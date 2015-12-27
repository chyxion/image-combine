#Image Combine

## Preview
2 Images
![Combine 2 Images](doc/images/combine-2.png)

3 Images
![Combine 3 Images](doc/images/combine-3.png)

4 Images
![Combine 4 Images](doc/images/combine-4.png) 

5 Images
![Combine 5 Images](doc/images/combine-5.png)

## Usage
### Add Maven Repository
```xml
    <repository>
    	<id>chyxion-github</id>
    	<name>Chyxion Github</name>
    	<url>http://chyxion.github.io/maven/</url>
    </repository>
```

### Add Maven Dependency
```xml
    <dependency>
        <groupId>me.chyxion</groupId>
        <artifactId>image-combine</artifactId>
        <version>0.0.1-RELEASE</version>
    </dependency>
```

### Use In Code
```java
    StringBuilder html = new StringBuilder();
    Scanner s = new Scanner(
    	getClass().getResourceAsStream("/sample.html"), "utf-8");
    while (s.hasNext()) {
    	html.append(s.nextLine());
    }
    s.close();
    FileOutputStream fout = new FileOutputStream("data.xls");
    fout.write(TableToXls.process(html));
    fout.close();
```

## Contacts

chyxion@163.com
