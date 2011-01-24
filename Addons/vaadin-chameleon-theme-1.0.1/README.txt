VAADIN CHAMELEON THEME
=====================


Add-on Package Contents:
===

 * 'vaadin-chameleon-theme-x.y.z.jar' - the core of the theme, that provides the basis for all color
   schemes.
 * other JAR's - different color schemes of the Chameleon Theme. File name corresponds to the name of
   the color scheme (and hence, theme).
    * The number of other JAR's depends on where you downloaded the package. You get five additional
    color schemes if you downloaded from the Directory, and only one if you downloaded this package
    from the online color editor (http://demo.vaadin.com/chameleontheme).
   
 Below items are only found from packages downloaded from the Directory:
 
 * Color schemes
    * The default Directory package includes four different color schemes:
       * "chameleon-blue" - gray and bright blue
       * "chameleon-green" - light green and bright green
       * "chameleon-dark" - a very dark theme with desaturated purple accents
       * "chameleon-vaadin" - uses some of the Vaadin brand colors (beige and blue)
       * The default "chameleon" theme is the un-colorized version of the theme (grayscale)

 * 'chameleon-theme-editor.war' - the color scheme editor, deployable to any Java application server.
 * META-INF/MANIFEST.MF - The Vaadin Add-on manifest file.
 
 
 Usage:
 ===

 * Copy all JAR-files to your projects WEB-INF/lib folder. The 'vaadin-chameleon-theme-x.y.z.jar' only 
   needs to be added once, it provides the basis for all color schemes. Other JAR's are for 
   different color schemes.
   
 * Add setTheme("chameleon-blue") to your Application.init to use the theme (the actual theme name
   depends on the color scheme used).
    * Using setTheme("chameleon") will result in using the un-colorized version of the theme.


Color Scheme Editor:
===

 * The editor can be found online at http://demo.vaadin.com/chameleontheme, but if you wish to run
   it locally, you can deploy the 'chameleontheme.war' to any application server you have 
   available (e.g. Jetty or Tomcat)