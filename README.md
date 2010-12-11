# README

## Description
The toolkit is a set of utilities to access, look up and store [OpenSymphony's PropertySet](http://www.opensymphony.com/propertyset/) data. The thing is - no one really knows what's the best and the most painless process to store JIRA items configuration, let's say for your **__plug-in A__** at both plug-in and Project levels.
The purpose and the intent of this project is to provide You as Plug-in developer with the toolkit and ready to use samples and snippets to choose from to make thing work. 

As the project name states - it uses PropertySet as underlying storage implementation. It might not be the best choice for every plug-in out there in the wild world, still it's rather usable for most use cases I had so far.

## Feature Overview
- Database persistence using [PropertySet](http://www.opensymphony.com/propertyset/) for underlying data storage
- Has no additional JIRA dependencies and provides enhanced PpropertySet API sub-set
- Provides a rich set of out-of box list of _Scope_ objects. (_Scope_ defines the sandbox of the PropertySet storage instance). At
 the moment following configuration options are supported
  - Named configuration (**Example:** My Gadget1 Configuration)
  - Action configuration (**Example:** Your Plugin may contain _Administration_ action,
which may require to persist and access administrator input).
  - dsd
tbd

## See, It's Alive - Sample Application Demo

As you may notice, the project code consists from two separate and completely valid Atlassian JIRA plugins.

    atlas-mvn -f ../propertyset-storage-toolkit/pom.xml  clean package install
      && atlas-mvn clean
      && atlas-integration-test --plugins "com.atlassian.plugins.studio.propertyset.storage:propertyset-storage-toolkit:1.0-SNAPSHOT"
      && atlas-debug --plugins "com.atlassian.plugins.studio.propertyset.storage:propertyset-storage-toolkit:1.0-SNAPSHOT"

## Installation
See [Managing Jira's Plug-ins ](http://confluence.atlassian.com/display/JIRA/Managing+JIRA's+Plugins) page for more details.

## Tradeoffs & Known Features
- Has not investigated performance problems possible in cases the approach will be over-used. **_The recommendation is use for
configuration data storage mostly._**


## How-To Recipes
.. TDB .. Now we are cooking.

## Other stuff 
