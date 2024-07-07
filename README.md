# What is Ares Guard?

Ares Guard is a java application which allows you to automatically configure Apache2 to allow only authorized indexing
bots to access your website.

# How does it work?

The software, executable from the command line, after specifying the path to the root folder of your site,
makes a REST call to the official GoogleBot and BingBot sites (functionality easily extendable to
other search engines). After having retrieved the list of official IP addresses from **official sources**, the
application adds authorization rules for these IPs in the .htaccess file, **without overwriting** the file.
If the authorized IPs have been previously added by the software, the software will rewrite them, leaving the contents
added by the user intact.
After modifying the `.htaccess` file, the application **overwrites** the `robots.txt` file, allowing access only to
GoogleBot and BingBot. As I said, this feature could be extended to other bots.

# How can the software be used?

My idea is to take advantage of Linux's crontab to run the jar periodically. The application would then be executed
automatically, updating the list of allowed IP addresses.
In order to generate or regenerate Apache2 rules it is necessary to simply pass the path to the root of the website as a
single parameter of the jar. For example:

In order to print the new rules on the screen:

```
java -jar ares-guard.jar "/var/www/my-website.com"
```

In order to print on the screen and **write** the new rules on file:

```
java -jar ares-guard.jar "/var/www/my-website.com" -w
```

# Download

The jar file can be downloaded from [here](https://github.com/goto-eof/ares-guard/blob/master/jar).

<img src="https://andre-i.eu:8080/api/v1/ipResource/custom.png?host=https://github.com/goto-eof/ares-guard" />