rmdir /S /Q docs
mkdir docs
javadoc -classpath ../lib/j2ee.jar;../src/container -d docs -windowtitle "Pluto Object Model" -header "Pluto Object Model" org.apache.pluto.om org.apache.pluto.om.common org.apache.pluto.om.entity org.apache.pluto.om.portlet org.apache.pluto.om.servlet
pause