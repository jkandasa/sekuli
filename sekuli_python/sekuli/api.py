"""
    Sekuli client implementation in Python.
"""
import base64
import os

from .sekuli import SekuliBaseClient

SIKULI_GRID_PATH = '/grid/admin/SekuliGridServlet/session/{sessionId}/SekuliNodeServlet'
SIKULI_NODE_PATH = '/extra/SekuliNodeServlet'


class SekuliClient(object):
    """
    Client class. User should this class to perform operations

    Arguments:

        hostname
            hostname of the selenium grid or node. default: localhost

        port
            port number of the selenium grid or node. default: 4444

        connection_type
            we can access remote selenium node via grid or directly to node.
            default: grid
            options: grid, node

        session_id
            session id of the current session. session id will be taken from selenium remote driver.
            when we go with "node" as connection type. session id is not required.
    """
    # pylint: disable=too-many-instance-attributes, too-few-public-methods
    def __init__(self, hostname='localhost', port=4444, connection_type='grid', session_id=None):
        self.hostname = hostname
        self.port = port
        self.connection_type = connection_type.lower()
        self.session_id = session_id

        # update actual url
        self.url = 'http://{}:{}'.format(self.hostname, self.port)
        if self.connection_type == 'grid':
            self.url = self.url + SIKULI_GRID_PATH.format(self.session_id)
        else:
            self.url = self.url + SIKULI_NODE_PATH

        # load clients
        self.screen = SekuliScreenClient(self.url)
        self.keyboard = SekuliKeyboardClient(self.url)
        self.mouse = SekuliMouseClient(self.url)


class SekuliScreenClient(SekuliBaseClient):
    """
    Class to perform screen operations
    """
    def __init__(self, url):
        SekuliBaseClient.__init__(self, url)

    def set_screen(self, screen_id):
        """
        Set screen number for sikuli operations. Most of the time we do operations on screen id 0.
        """
        _cmd = self.get_command('screen', 'setScreen', {'screenId': screen_id})
        return self._post(data=_cmd)

    def get_screen(self):
        """
        Get current screen where sikuli performs operations.
        """
        _cmd = self.get_command('screen', 'getScreen', {})
        return self._post(data=_cmd)

    def screens_count(self):
        """
        Returns number of screens available.
        """
        _cmd = self.get_command('screen', 'getScreens', {})
        return self._post(data=_cmd)['count']

    def capture(self, file_path, region=None):
        """
        Capture the screen and save it on the specified location.
        If region is not supplied, whole screen will be taken
        region will be looking as follows,
        {
            'x': 0,
            'y': 0,
            'width': 120,
            'height': 120
        }
        """
        if region:
            _cmd = self.get_command('screen', 'captureBase64', region)
        else:
            _cmd = self.get_command('screen', 'captureBase64', {})
        _base64_string = self._post(data=_cmd)['base64String']
        # create and update file
        _dir, _filename = os.path.split(file_path)
        if not os.path.exists(_dir):
            os.makedirs(_dir)
        with open(file_path, mode='w') as _file:
            _file.write(base64.decodestring(_base64_string))

    def add_target(self, file_path, target_name=None, similarity=0.8):
        """
        Add image target to remote sikuli machine.
        Provide file_path, target_name(optional) and similarity(optional)
        default target_name will be file name of the image
        default similarity will be 0.8
        When directory name provided as a file path,
        all the images(png) will be added recursively to the remote machine.
        returns tuple (count, mapping dict of the current operation)
        """
        # pylint: disable=too-many-locals
        count = 0
        _targets = {}
        _files = []
        if os.path.isdir(file_path):
            for root, _, filenames in os.walk(file_path):
                for filename in filenames:
                    if filename.lower().endswith('.png'):
                        _files.append(os.path.join(root, filename))
            target_name = None
        else:
            _files.append(file_path)
        for _filename in _files:
            _target_name = target_name
            _base64_string = ''
            if not _target_name:
                _dir, _target_name = os.path.split(_filename)
            with open(_filename, mode='r') as _file:
                _base64_string = base64.b64encode(_file.read())
            _cmd = self.get_command('screen', 'addTarget', {
                'base64String': _base64_string,
                'targetName': _target_name,
                'similarity': similarity
                })
            count = self._post(data=_cmd)['count']
            _targets[_target_name] = _filename
        return count, _targets

    def find(self, target_name):
        """
        find the image on the screen and return region if available.
        """
        _cmd = self.get_command('screen', 'find', {'targetName': target_name})
        return self._post(_cmd)

    def find_all(self, target_name):
        """
        This method similar to find method. It returns all the matches as list of regions
        """
        _cmd = self.get_command('screen', 'findAll', {'targetName': target_name})
        return self._post(_cmd)

    def click(self, target_name):
        """
        Perform mouse click on the specified image
        """
        _cmd = self.get_command('screen', 'click', {'targetName': target_name})
        self._post(_cmd)

    def list_targets(self):
        """
        list all the stored images on remote sikuli machine
        """
        _cmd = self.get_command('screen', 'listTargets', {})
        return self._post(data=_cmd)

    def clear_all_targets(self):
        """
        Clear all the images on the remote Sikuli machine.
        """
        _cmd = self.get_command('screen', 'clearAllTargets', {})
        return self._post(data=_cmd)['count']


class SekuliKeyboardClient(SekuliBaseClient):
    """
    Class to perform keyboard operations
    """
    def __init__(self, url):
        SekuliBaseClient.__init__(self, url)

    def paste(self, text):
        """
        Paste the given text to remote machine in to the current selection.
        """
        self._post(data=self.get_command('keyboard', 'paste', {'value': text}))

    def feed(self, text):
        """
        Do type the given string into the remote machine on the selected area.
        """
        self._post(data=self.get_command('keyboard', 'type', {'value': text}))

    def select_all(self):
        """
        Perform "select all" on remote machine. This action may executed before "copy".
        """
        self._post(data=self.get_command('keyboard', 'selectAll', {}))

    def copy(self):
        """
        Perform "copy" operation on remote machine and return selected text
        """
        _cmd = self.get_command('keyboard', 'copy', {})
        _data = self._post(data=_cmd)
        if 'value' in _data:
            return _data['value']


class SekuliMouseClient(SekuliBaseClient):
    """
    Class to perform mouse operations
    """
    def __init__(self, url):
        SekuliBaseClient.__init__(self, url)

    def location(self):
        """
        get the current location of the mouse pointer.
        """
        return self._post(data=self.get_command('mouse', 'location', {}))

    def click(self, **kwargs):
        """
        click on the specified location or region.
        In cause if region passed, click operation will be performed on center of the region.
        """
        _cmd = None
        if 'region' in kwargs:
            _cmd = self.get_command('mouse', 'clickRegion', kwargs['region'])
        elif 'location' in kwargs:
            _cmd = self.get_command('mouse', 'clickLocation', kwargs['location'])
        else:
            raise BadInputException("Either 'region' or 'location' required")

        self._post(data=_cmd)


class BadInputException(Exception):
    """
    This exception will be used when user passed bad/missing values as input.
    """
    pass
