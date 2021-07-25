### 需求

#### 实现多个配置来源

* `InMemoryCounfigSource`
* `Classpath`
  * `ClasspathPropertiesConfigSource`
  * `ClasspathYamlConfigSource`
  * `ClasspathXmlConfigSource`
  * `ClasspathJsonConfigSource`
* `database`
* `java system properties`
* `java system environment`
* `ExtraFilePath`
  * `ExtraPropertiesConfigSource`
  * `ExtraYamlConfigSource`
  * `ExtraXMLConfigSource`
  * `ExtraJSONConfigSource`
* `os`

#### 优先级

`InMemoryConfigSource > ClasspathPropertiesConfigSource > ClasspathYamlConfigSource > ClasspathXmlConfigSource > ClassJsonConfigSource > DatabaseConfigSource > JavaSystemPropertiesConfigSource > JavaSystemEnvironmentConfigSource > ExtraPropertiesConfigSource > ExtraYamlConfigSource > ExtraXMLConfigSource > ExtraJsonConfigSource > OsConfigSource`

优先级越高，`ordinal value`越小

#### Converter

* `IntegerConverter` 