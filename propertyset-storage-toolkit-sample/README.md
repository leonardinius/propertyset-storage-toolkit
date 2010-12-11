- Launch

      atlas-mvn -f ../propertyset-storage-toolkit/pom.xml clean package install \
        && atlas-mvn clean                                                      \
        && atlas-integration-test --plugins \
           "com.atlassian.plugins.studio.propertyset.storage:propertyset-storage-toolkit:1.0-RELEASE" \
        && atlas-debug --plugins \
           "com.atlassian.plugins.studio.propertyset.storage:propertyset-storage-toolkit:1.0-RELEASE"
- Enjoy
