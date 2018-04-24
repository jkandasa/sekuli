# Sekuli Python client library

This library helps to access Sikuli remotely(via selenium grid or node).

## Usage
----
* Setup virtual environment and installed requirements
```
$ virtualenv .env
$ source ./.env/bin/activate
$ pip install -r requirements.txt
```

* Initialize client and execute some screen commands
```python
>>> from sekuli.api import SekuliClient
>>> client = SekuliClient(hostname='192.168.122.23', port=5555, connection_type='node')
>>> client.screen.get_screen()
{u'screenId': 0}
>>> client.screen.capture(file_path='/tmp/full_desktop.png')
>>> client.screen.capture(file_path='/tmp/region.png', region={'x': 10, 'y': 10, 'width': 120, 'height': 160})
>>> client.screen.add_target(file_path='/tmp/settings_ubuntu.png', target_name='settings_icon', similarity=0.8)
(1, {'settings_icon': '/tmp/settings_ubuntu.png'})
>>> client.screen.list_targets()
[u'settings_icon']
>>> client.screen.find(target_name='settings_icon')
{u'width': 23, u'screen': {u'screenId': 0}, u'height': 20, u'states': {}, u'rois': [], u'score': 0.999954879283905, u'y': 3, u'x': 996}
>>> client.screen.find_all(target_name='settings_icon')
[{u'width': 23, u'screen': {u'screenId': 0}, u'height': 20, u'states': {}, u'rois': [], u'score': 0.999954879283905, u'y': 3, u'x': 996}]
>>> client.screen.click(target_name='settings_icon')
>>> 

```