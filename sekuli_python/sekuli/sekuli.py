"""
    This file contains SekuliBaseClient methods
"""
import json

import requests


class SekuliBaseClient(object):
    '''
    This SekuliBaseClient act as a base class for all the clients like screen, keyboard and mouse.

    Arguments:

        url
            full URL to access remote sikuli via selenium grid or selenium node.
    '''
    # pylint: disable=too-few-public-methods
    def __init__(self, url):
        self.url = url

    def _post(self, data):
        _response = requests.post(
            self.url,
            headers={'Content-Type': 'application/json'},
            data=json.dumps(data))
        _data = _response.json()
        if _response.status_code != 200:
            raise BadResponseException(_data)
        if 'result' in _data and _data['result'] == 'SUCCESS':
            if 'response' in _data:
                return _data['response']
            return None
        raise BadResponseException(_data)

    @classmethod
    def get_command(cls, module, command, parameters):
        """
        This command function returns command format dict object.
        """
        return {
            "module": module,
            "command": command,
            "parameters": parameters
        }


class BadResponseException(Exception):
    """
    If there is an error with request/response. This exception will be raised.
    """
    pass
