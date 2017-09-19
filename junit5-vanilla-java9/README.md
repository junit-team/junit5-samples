# junit5-vanilla-java9

The `junit5-vanilla-java9` project demonstrates how to execute JUnit Jupiter
tests on the module-path.
Be sure to consult the
[Quick-Start Guide](http://openjdk.java.net/projects/jigsaw/quick-start)
describing the Jigsaw Module System first.


## JShell as Build Tool

There's **no** external build tool involved. The pojo `Build.java` stored in
`src/build` directory is sourced into a JShell session by the `build.jsh` script.
`Build.java` uses default JDK Tool provisioning to compile and launch test runs
in a platform-independent manner.

Either start the sample via `jshell build.jsh` or mount the `src/build` as a
source directory within your IDE and "press play on tape", i.e. run main method
of class `Build`.


## Directory Layout and Build Commands

### **`deps`**

The `deps` directory contains all modules this sample project depends on.
If it does not exist `Build` creates and fills it with resolved jar files.
 

### **`src/main`**
The `src/main` directory contains the sources of a sample library and an
application that uses/reads/requires the exported api of the library:
```
└─ src/main
   ├─ com.example.project
   └─ org.example.library
```

Compile main module `com.example.project`:
```
javac
  -d mods/main
  --module-path deps
  --module-source-path src/main
  --module com.example.project
```

### **`src/test`**
The `src/test` directory contains the sources for testing the library
and application module -- legacy style.
Strong module boundaries are lifted by patching in main module sources.
Note how the same module names are used as in the main source directory:
```
└─ src/test
   ├─ com.example.project
   └─ org.example.library
```

Compile test module `com.example.project`:
```
javac
  -d mods/test
  --module-path mods/main:deps
  --module-source-path src/test
  --patch-module com.example.project=src/main/com.example.project
  --module com.example.project
```

Run all tests that can be found on the module-path:
```
java 
  --module-path mods/test:mods/main:deps
  --add-modules ALL-MODULE-PATH
  --module org.junit.platform.console
  --scan-module-path
╷
└─ JUnit Jupiter ✔
   ├─ CalculatorTests ✔
   │  └─ JUnit 5 test! ✔
   └─ ProtectedVersionTests ✔
      └─ versionEquals4711() ✔
```

### **`src/user`**
Contains the sources for testing the library and application -- from an
end-user point-of-view. The tests are only able to see and use the exported
parts of the main modules.
```
└─ src/user
   └─ integration
```

Run all tests that can be found on the module-path:
```
java
  --module-path mods/user:mods/main:deps
  --add-modules ALL-MODULE-PATH
  --module org.junit.platform.console
  --scan-module-path
╷
└─ JUnit Jupiter ✔
   └─ IntegrationTests ✔
      ├─ projectIsAccessable() ✔
      ├─ explicitModuleNames() ✔
      └─ libraryIsAccessable() ✔
```
