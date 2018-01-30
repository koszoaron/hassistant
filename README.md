# HAssistant
Android frontend for Home Assistant

Main goals:
* Provide a WebView to open the HASS user interface.
* Use different URLs depending on network connections, for example use a different URL when connected to the home Wi-Fi.
* Update location data:
  * GPSLogger and OwnCloud API support;
  * selectable fine (GPS) or coarse (network) precision;
  * support zones;
  * support reporting location/zone when connecting to or disconnecting from the home network.
* Display notifications coming from the HASS server.
  * Use HTML5 push notifications if possible, investigate GCM usage.
* Implement an Android TV UI for home control on the TV.
  * Marshmallow and later.
  * First, just display a HADashboard page, if it can be navigated using the remote.
  * Optional MQTT client for TV status reporting and remote control features.
  * Add Google Assistant support for voice control of home functions (through the app, without the standard Google Assistant integration).
* Implement native classes and views for HASS elements.
  * Maybe reuse/merge code from the excellent [HassDroid](https://github.com/Maxr1998/home-assistant-Android) project.
