<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="itm.image.*" %>
<%@ page import="itm.model.*" %>
<%@ page import="itm.util.*" %>
<%@ page import="com.sun.org.apache.xpath.internal.operations.Bool" %>
<!--
/*******************************************************************************
 This file is part of the WM.II.ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/
-->
<%

%>
<html>
    <head>
        <link rel="stylesheet" href="hoverimage.css">
    </head>
    <body>



        <%

            String tag = null;

            // ***************************************************************
            //  Fill in your code here! DONE
            // ***************************************************************

            // get "tag" parameter
            tag = request.getParameter("tag");
            // if no param was passed, forward to index.jsp (using jsp:forward)
            if (tag.isEmpty()) {
                %><jsp:forward page="index.jsp" /><%
            }



        %>

        <h1>Media that is tagged with <%= tag %></h1>
        <a href="index.jsp">back</a>


        <%
            // ***************************************************************
            //  Fill in your code here! DONE
            // ***************************************************************

            // get all media objects that are tagged with the passed tag


            // iterate over all available media objects and display them -> wird darunter in der For Schleife


            // get the file paths - this is NOT good style (resources should be loaded via inputstreams...)
            // we use it here for the sake of simplicity.
            String basePath = getServletConfig().getServletContext().getRealPath( "media"  );
            if ( basePath == null )
                throw new NullPointerException( "could not determine base path of media directory! please set manually in JSP file!" );
            File base = new File( basePath );
            File imageDir = new File( basePath, "img");
            File audioDir = new File( basePath, "audio");
            File videoDir = new File( basePath, "video");
            File metadataDir = new File( basePath, "md");
            MediaFactory.init( imageDir, audioDir, videoDir, metadataDir );

            // get all media objects
            ArrayList<AbstractMedia> media = MediaFactory.getMedia();

            int c=0; // counter for rowbreak after 3 thumbnails.
            // iterate over all available media objects
            for (AbstractMedia medium : media) {



                // handle images
                if (medium instanceof ImageMedia) {
                    // ***************************************************************
                    //  Fill in your code here! DONE
                    // ***************************************************************


                    // display image thumbnail and metadata
                    ImageMedia img = (ImageMedia) medium;
                    ArrayList<String> imgTags = img.getTags();
                    Boolean containsTag = false;
                    for(String s : imgTags) {
                        if (s.equals(tag)){
                            containsTag = true;
                            break;
                        }
                    }
                    if (containsTag == true) {
                        c++;

            %>

        <div style="width:300px;height:300px;padding:10px;float:left;">
            <div style="width:200px;height:200px;padding:10px;">
                <a href="media/img/<%= img.getInstance().getName()%>">
                    <img src="media/md/<%= img.getInstance().getName() %>.thumb.png" border="0"/>
                </a>
                <!--  show the histogram of the image on mouse-over and in CSS File-->
                <figure class="figure-top">
                    <img style="width:200px;height:200px;padding:10px;position:absolute;"
                         src="media/md/<%= img.getInstance().getName() %>.hist.png" border="0"/>
                </figure>
            </div>
            <div>
                Name: <%= img.getName() %><br/>
                Dimensions: <%= img.getWidth() %>x<%= img.getHeight() %>px<br/>
                Tags: <% for (String t : img.getTags()) { %><a href="tags.jsp?tag=<%= t %>"><%= t %>
            </a> <% } %><br/>
            </div>
            <%
                    }
                } else {
                }

            %>
        </div>
        <%
            if (c % 3 == 0) {
        %>
        <div style="clear:left"/>
        <%
                }

            } // for

        %>



    </body>
</html>
