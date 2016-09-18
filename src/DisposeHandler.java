import processing.core.*;

/**
 * Just a "book-keeping" class for telling the applet to close all music files before exiting 
 *
 */
public class DisposeHandler {
  MainApplet parent;
  DisposeHandler(MainApplet _parent)
  {
	parent = _parent;
    parent.registerMethod("dispose", this);
  }
   
  public void dispose()
  {      
    parent.closeMusic();
  }
}