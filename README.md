# composable4j

composable4j is a very simple declarative UI tool,
you can use it to build simple Java desktop applications.
If you have used jetpack-compose, you will feel very familiar with it.
Currently the supported functions are very limited.

## How to use
No need for any third-party library dependencies.
Just create a JFrameComposableApp object and 
pass a root node composable object to it. 

```java
public static void main(String[] args) {
    new JFrameComposableApp(new Box() {{
        modifier.size(800, 800);
        contentSupplier = content -> {
            content.add(new Text() {{
                text = "Hello World!";
                font = new Font("宋体", Font.PLAIN, 20);
                modifier.alignment = Alignment.Center;
            }});
        };
    }});
}
```
There are more usage examples in the test directory.

## License
This project adopts the MIT license, as detailed in the LICENSE document.

