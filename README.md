# Playerhider-Plugin
A Plugin to hide other players from player on Paper Servers running on 1.21.4

Usage
--------
Add all staff and media ranks to the vip group in luckperms.
Put the Luckperms jar into the plugins folder.
Connect to a database either mysql or mariadb.

See the Wiki for additional information.

Requirements
--------
Mariadb/Mysql
For this plugin to work you need to have a mysql/mariadb database setup and linked via the config file which is generated on the first start. 

Luckperms
You need Luckperms to be installed for this plugin to work, for the vips to be hidden.

Building
--------
After cloning this repository, build the project with Maven by running `mvn clean package` inside the playerhider folder and take the created jar out
of the `/target/` directory.

You need JDK 17 or newer to build ViaVersion.

Roadmap
--------
- [ ]  Add support for more database drivers
- [ ]  Make it possible to disable Hide Mods to make Luckperms and Database connections optional
- [ ]  Create support for more MC versions
- [ ]  Add better error handeling
