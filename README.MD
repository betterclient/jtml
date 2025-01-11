# HTMLUtil

Create stunning interfaces for minecraft with HTML.

# Usage

- Before we start: This is still a **WIP** library, you might have to do **drastic** changes to your codebase. This is an implementation of HTML Rendering and things may not be 100% accurate to browser rendering.  

##### Steps: 
- Create a new source file. (java/kotlin/groovy/whatever)
- Extend `HTMLDocument`:
  ```java
  public class YourClass extends HTMLDocument {
  
  }
  ```
- Create a constructor: 
  ```java
  public class YourClass extends HTMLDocument {
    public YourClass() {
        
    }
  }
  ```
- Add a super() call:
  ```java
  public class YourClass extends HTMLDocument {
    public YourClass() {
        //You have 3 options
        super(); //Empty document.
        super("HTML goes here"); //Document with text as source.
        super("myhtml.html"); //Document with asset as source.
    }
  }
  ```
- Call HTMLDocument.openAsScreen on your class:
  ```java
  new YourClass().openAsScreen();
  ```
- Congrats! You have created a simple document.

# Other useful stuff

- You can use the prefix `Translate->` before a string to automatically translate it.

Example:
```html
<span>Translate->menu.modded</span>
```
This will get interpreted as:
```html
<span> (Modded)</span>
```
*Note: This won't work on translations with parameters*


# Credits

- jsoup: Library used for parsing HTML

# Support

Since this implementation is from scratch, support for some elements/properties is missing.

### ❌ NO JAVASCRIPT. 
HTMLUtil has an API for doing basically everything you can do with javascript, using java.

- Supported Elements
```
<span>
<br>
<button>
<body>
```
- Supported properties `* means anything that works in normal css`
```
display: inline | flex;
background-color: *;
color: *;
justify-content: left | center | right;
align-items: start | center | end;
flex-direction: column | row;
```
- Custom properties to help with coding:
```
width-offset: 2px; (2px is default, this is the width offset of a text node)
```

# Other helpful things
- You don't need the entire html document, just use the body is enough!