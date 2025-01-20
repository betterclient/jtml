# JTML

Create stunning interfaces for minecraft with HTML.

# Usage

- Before we start: This is still a **WIP** library, you might have to do **MASSIVE** changes to your codebase. This implementation is from scratch and things may not be 100% accurate to browser rendering.
 
- Create a new source file.
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
        super(/*your service of choice*/); //Empty document.
        super(/*your service of choice*/, "HTML goes here"); //Document with text as source.
        super(/*your service of choice*/, "myhtml.html"); //Document with asset as source.
    }
  }
  ```
- Pick a service (eg: `new MCTMLService()`)
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
```html
<span>
<br>
<button>
<div>
<label>
<input>
<body>
<h1-6>
```
- Supported properties `* means anything that works in normal css`
```css
display: inline | flex | grid;
background-color: *;
color: *;
text-decoration: strikethrough | underline | none;
border-radius: *;

justify-content: left | center | right;
align-items: start | center | end;
flex-direction: column | row;

grid-template-columns: *;
grid-template-rows: *;
grid-column: *;
grid-row: *;

margin: *;
margin-top: *;
margin-bottom: *;
margin-left: *;
margin-right: *;

padding: *;
padding-top: *;
padding-bottom: *;
padding-left: *;
padding-right: *;
```
- Custom properties to help with coding:
```
width-offset: 2px; (2px is default, this is the width offset of a text node)
```

# Other helpful things
- You don't need the entire html document, just the body is enough!