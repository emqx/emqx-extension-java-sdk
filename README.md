# Extension For Java SDK

This repostory provide a Java SDK based on [emqx-extension-hook](https://github.com/emqx/emqx-extension-hook).

## Requirements

- JDK 1.8+

- Depend on `erlport.jar` (The communication module used in emqx-extension-hook)

## SDK edition & EMQ X Broker edition

| SDK edition | EMQ X Broker edition |
| ----------- | -------------------- |
| 1.0.0       | 4.2.0                |
| unsupport   | before 4.2.0         |

## Get Started

1. First of all, create your Java project.

2. See `SDK edition & EMQ X Broker edition` part. Chose your SDK edition.Depends on EMQ X Broker edition.

   Download the [io.emqx.extension.jar](https://search.maven.org/search?q=a:emqx-extension-java-sdk) and [erlport.jar](https://github.com/emqx/emqx-extension-java-sdk/raw/master/deps/erlport-v1.2.2.jar)

3. Add the sdk: `io.emqx.extension.jar` and `erlport.jar` to your project dependency.

   If your project is a maven project, add a dependency in your maven project `pom.xml` `<dependencies></dependencies>`.

   ```xml
   <dependency>
     <groupId>io.emqx</groupId>
     <artifactId>emqx-extension-java-sdk</artifactId>
     <!-- Chose your SDK edition.Depends on EMQ X Boker edition. -->
     <!-- Change version to your chosen SDK edition -->
     <version>1.0.0</version>
   </dependency>
   ```
   
   Change `<version>version</version>` to your chosen SDK edition, like `<version>1.0.0</version>`.
   
4. Copy the `examples/SampleHandler.java` into your project.

5. Successfully compile all the source codes.

Note: NOT read/write `System.out.*` and `System.in` Stream. They are used to communicate with EMQ X.

## Deploy

After compiled all source codes, you should deploy the `sdk` and your class files into EMQ X.

1. Copy the `io.emqx.extension.jar` to `emqx/data/extension` directory.
2. Copy your class files, e.g: `SampleHandler.class` to `emqx/data/extension` directory.
3. Modify the `emqx/etc/plugins/emqx_extension_hook.conf` file. e.g:

```protperties
exhook.drivers = java
## Search path for scripts or library
exhook.drivers.java.path = data/extension/
exhook.drivers.java.init_module = SampleHandler
```
4. Execute `bin/emqx console` to start EMQ X and load the `emqx_extension_hook` plugin.
5. Try to establish a MQTT connection and observe the console output.

## Examples

See: examples/SampleHandler.java

## License

Apache License v2

## Authors

- [Chong Yuan](https://github.com/chongyuanyin)
