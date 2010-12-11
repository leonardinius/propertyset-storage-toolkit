# README

## Description
The toolkit is a set of utilities to access, look up and store [OpenSymphony's PropertySet](http://www.opensymphony.com/propertyset/) data. The thing is - no one really knows what's the best and the most painless process to store JIRA items configuration, let's say for your **__plug-in A__** at both plug-in and Project levels.
The purpose and the intent of this project is to provide You as Plug-in developer with the toolkit and ready to use samples and snippets to choose from to make thing work. 

As the project name states - it uses PropertySet as underlying storage implementation. It might not be the best choice for every plug-in out there in the wild world, still it's rather usable for most use cases I had so far.

## Compatability
The plugin is developed as JIRA Atlassian Plugin v2. This limits it's usage to JIRA > 4x.
On the other hand - it does not have any dependency one new stuff and should work correctly one JIRA 3xml as well (needs adjustments).
Please informe me if you would try to modify it for earlier versions.

## Feature Overview
- Database persistence using [PropertySet](http://www.opensymphony.com/propertyset/) for underlying data storage
- Has no additional JIRA dependencies and provides enhanced PropertySet API sub-set
- Could be used both as JAR dependency bundled with your plugin OR as separate plugin, depending one your current needs.
- Provides a rich set of out-of box list of _Scope_ objects. (_Scope_ defines the sandbox of the PropertySet storage instance). At
 the moment following configuration options are supported
  1. Named configuration scope (Example: My Gadget1 Configuration)
  1. Action configuration scope (Example: your plugin may contain _Administration_ action,
which may require to persist and access administrator input).
  1. Project configuration scope (Example: you may associate configuration information with project instances)
  1. Issue configuration scope (Example: you may associate configuration information with project instances)
- The list of scopes is not limited and you may come-up with your own _Scope_

## Sample Code

#### 1. Action persistence in action

     private final StorageService storageService; // IoC by framework via Constructor Dependency Injection
     ...

     // data fields
     private boolean adminOnly;
     private String helloText;
     private String welcomeText;

     private StorageFacade getStorage()
     {
         return storageService.actionStorage(this);
     }

    
     private void loadConfig(StorageFacade storage)
     {
         setAdminOnly(storage.getBoolean(ADMIN_ONLY));
         setHelloText(StringUtils.defaultString(storage.getString(HELLO_TEXT), "Hello, "));
         setWelcomeText(StringUtils.defaultString(storage.getString(WELCOME_TEXT), "World"));
     }

     private void saveConfig(StorageFacade storage)
     {
         storage.setBoolean(ADMIN_ONLY, isAdminOnly());
         storage.setString(HELLO_TEXT, getHelloText());
         storage.setString(WELCOME_TEXT, getWelcomeText());
     }

#### 2. DTO Object data persistence
    // class I want to save as My configuration data holder
    private static class MySerializable implements Serializable
    {
        private String text;
        private Long number;
        private Map<String, String> mapping;
        
        ...
        getters, setters
        ...
        hashCode, equals
    }


    @ToolkitTest
    protected void constantScopeSaveDeleteForObject() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("object");

        MySerializable object = new MySerializable(10L, "data data", Maps.<String, String>newHashMap(ImmutableMap.of("1", "data1", "sdsd",
                "data2")));
        stateTrue("First object access should be null", facade.getObject("object") == null);
        facade.setObject("object", object);
        stateTrue("object key should exist now", facade.exists("object"));
        stateTrue("" + object + "=" + facade.getObject("object"), facade.getObject("object").equals(object));
        facade.setObject("object", null);
        not("object key should NOT exist now", facade.exists("object"));

        facade.setObject("object", object);
        stateTrue("long key should exist now", facade.exists("object"));
        stateTrue("delete object should work", facade.remove("object"));
        not("long key should NOT exist now", facade.exists("object"));
    }

## How do I get started?

As you may notice, the project code consists from two separate and completely valid Atlassian JIRA plugins. It contains the persistence
toolkit itself as well as simple samples application with bundled-integration test and WebWork configurable action in action.

You may proceed as it follows:
* Get the sources (if you are unfamiliar with Git or Github, help is [here](http://help.github.com/))
* If you still don't hane Atlassian PDK installed - you probably are reading wrong README then. (it's [out there](http://confluence.atlassian.com/display/DEVNET/Developing+your+Plugin+using+the+Atlassian+Plugin+SDK).I used atlassian-plugin-sdk-3.2.3 at the moment of writing.
* Launch the terminal and `cd` to propertyset-storage-toolkit-sample folder
* Execute the following command

    atlas-mvn -f ../propertyset-storage-toolkit/pom.xml clean package install \
      && atlas-mvn clean                                                      \
      && atlas-integration-test --plugins \
         "com.atlassian.plugins.studio.propertyset.storage:propertyset-storage-toolkit:1.0-SNAPSHOT" \
      && atlas-debug --plugins \
         "com.atlassian.plugins.studio.propertyset.storage:propertyset-storage-toolkit:1.0-SNAPSHOT"
* Wait

The command above will compile and install into the local repository propertyset-storage-toolkit plugin artifact,
then it will test it and launch sample application.

## Sample Application Tutorial

If you succeed with the steps above - then you have proceed and open http://localhost:2990/jira/ link. You will notice new dropdown Menu Item "PropertySet Toolkit". Menu items descriptions:

- Integration Test Results - link to servlet with integration test data (servlet executes test suite and provides the execution summary).
- Manual - link to documentation (More to come)
- Configurable Action Example - Sample [WebWork Plugin Module](http://confluence.atlassian.com/display/JIRA/Webwork+plugin+module) with confiration pesistence enabled.


## Installation as separate plugin
See [Managing Jira's Plug-ins ](http://confluence.atlassian.com/display/JIRA/Managing+JIRA's+Plugins) page for more details.

## Tradeoffs & Known Features
- Has not investigated performance problems possible in cases the approach will be over-used. **_The recommendation is use for
configuration data storage mostly._**


## How-To Recipes
.. TDB .. Now we are cooking, more to come.

