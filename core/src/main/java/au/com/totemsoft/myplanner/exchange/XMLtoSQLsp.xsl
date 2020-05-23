<?xml version="1.0" encoding="utf-8"?>
<!-- ===========================================================
  Category:       XMLtoText
  Author:         
                  
  Created:        
  Description:-
    This stylsheet will convert an xml file that is
    attribute-based to the text one that contains the function calls
    to SQL Server stored procedures.  In this  stylesheet, the
    original elements represent records from database tables
    with the column names as a series of attributes. The output of this transformation
    will be a number of function calls delimited with 'RecordDelimiter'.
================================================================ -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output  cdata-section-elements="node" method="text"/>
	<xsl:variable name="lapos">&apos;</xsl:variable>
	<xsl:variable name="sapos">&#x26;apos;</xsl:variable>
	<xsl:template match="/data">
		<xsl:for-each select="*">
<!-- Execute header-->			
			<xsl:text>EXEC sp_import_</xsl:text><xsl:value-of select="name()"/><xsl:text> </xsl:text>
<!-- For every attribute create the entry like @attributename = attributevalue-->								
					<xsl:for-each select="@*">
						<xsl:text>@</xsl:text><xsl:value-of select="name()"/><xsl:text>='</xsl:text>
						<xsl:if  test="not(contains(.,$lapos))">
								<xsl:value-of select="." />
						</xsl:if>	
						<xsl:if  test="(contains(.,$lapos))">
<!--								<xsl:value-of select="substring-before(.,$lapos)" /><xsl:value-of select="$sapos"/>-->
								<xsl:value-of select="substring-before(.,$lapos)" /><xsl:value-of select="$lapos"/><xsl:value-of select="$lapos"/>								
				       			<xsl:call-template name="AposParser">
									<xsl:with-param name="Word" select="substring-after(.,$lapos)"/>
								</xsl:call-template>


						</xsl:if>

						<xsl:text disable-output-escaping="no">',</xsl:text>
					</xsl:for-each>
<!-- Record delimiter-->								
				<xsl:text> RecordDelimiter </xsl:text>
		</xsl:for-each>
		
e>
	</xsl:template>
	<xsl:template name = "AposParser">
 		<xsl:param name="Word" select="&#39;Internet&#39;" />
    	<xsl:if test="string-length($Word) &gt; 0 ">

		<xsl:if test="string-length(substring-before($Word,$lapos)) &gt; 0 ">
<!--			<xsl:value-of select="substring-before($Word,$lapos)"/><xsl:value-of select="$sapos"/>-->
			<xsl:value-of select="substring-before($Word,$lapos)"/><xsl:value-of select="$lapos"/><xsl:value-of select="$lapos"/>
		</xsl:if>

								

		<xsl:if test="string-length(substring-before($Word,$lapos)) &lt; 1 and not(contains($Word,$lapos))">
			<xsl:value-of select="$Word"/>
		</xsl:if>
		<xsl:if test="string-length(substring-before($Word,$lapos)) &lt; 1 and (contains($Word,$lapos))">
<!--			<xsl:value-of select="$sapos"/>-->
			<xsl:value-of select="$lapos"/><xsl:value-of select="$lapos"/>
		</xsl:if>

<!--		<xsl:text  disable-output-escaping="yes">&lt;/OPTION&gt;</xsl:text>-->
		<xsl:call-template name="AposParser">
			<xsl:with-param name="Word" select="substring-after($Word,$lapos)"/>
		</xsl:call-template>
    </xsl:if>
 </xsl:template>
</xsl:stylesheet>