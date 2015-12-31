#Image Combine

## Preview
### 2 Images
![Combine 2 Images](/doc/images/combine-2.png)

### 3 Images
![Combine 3 Images](/doc/images/combine-3.png)

### 4 Images
![Combine 4 Images](/doc/images/combine-4.png) 

### 5 Images
![Combine 5 Images](/doc/images/combine-5.png)

## Usage

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
    saveImage(ic.combine(
        readImage("joker.jpg"), 
        readImage("lufy.png"), 
        readImage("zoro.png"), 
        size),
    "combine-3.png");
```
See test cases to get more info.

## Contact

chyxion@163.com
