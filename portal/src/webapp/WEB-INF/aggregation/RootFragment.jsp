<!-- 
Copyright 2004 The Apache Software Foundation
Licensed  under the  Apache License,  Version 2.0  (the "License");
you may not use  this file  except in  compliance with the License.
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed  under the  License is distributed on an "AS IS" BASIS,
WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
implied.

See the License for the specific language governing permissions and
limitations under the License.
-->
<%@ page session="true" buffer="none" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.pluto.portalImpl.util.Parameters" %>
<%@ page import="org.apache.pluto.portalImpl.core.PortalURL" %>
<%@ page import="org.apache.pluto.portalImpl.core.PortalEnvironment" %>
<%@ page import="org.apache.pluto.portalImpl.aggregation.Fragment" %>
<%@ page import="org.apache.pluto.portalImpl.aggregation.navigation.AbstractNavigationFragment" %>
<jsp:useBean id="fragment" type="org.apache.pluto.portalImpl.aggregation.Fragment" scope="request" />
<html>
<%@ include file="./Head.jsp" %>
<body marginwidth="0" marginheight="0">
<%@ include file="./Banner.jsp" %>
<table border="0" cellpadding="0" cellspacing="2" width="100%">
  <tr>
    <td width="30%" valign="top">
<%
        Iterator childIterator = fragment.getChildFragments().iterator();

        while (childIterator.hasNext()) {
            Fragment subfragment = (Fragment)childIterator.next();

            if (subfragment instanceof AbstractNavigationFragment)
            {
                subfragment.service(request, response);
                break;
            }

        }
%>
    </td>
    <td valign="top">
<%
        childIterator = fragment.getChildFragments().iterator();

        while (childIterator.hasNext()) {
            Fragment subfragment = (Fragment)childIterator.next();

            if (!(subfragment instanceof AbstractNavigationFragment))
            {
                subfragment.service(request, response);
            }

        }
%>
    </td>
  </tr>
</table>
</body>
</html>