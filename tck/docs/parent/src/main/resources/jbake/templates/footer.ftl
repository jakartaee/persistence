<#-- a footer template fragment included in the page template -->
<hr />

<table width="90%" id="bottom-nav" cellspacing="0" cellpadding="0">
    <colgroup>
        <col width="12%"/>
        <col width="12%"/>
        <col width="*"/>
    </colgroup>
    <tr>
        <#if content.prev??>
        <td align="left">
        <a href="${content.prev}">
            <span class=" vector-font"><i class="fa fa-arrow-circle-left" aria-hidden="true"></i></span>
            <span style="position:relative;top:-2px;">Previous</span>
        </a>
        </td>
        </#if>

        <#if content.next??>
        <td align="left">
        <a href="${content.next}">
            <span class="vector-font"><i class="fa fa-arrow-circle-right vector-font" aria-hidden="true"></i></span>
            <span style="position:relative;top:-2px;">Next</span>
        </a>
        </td>
        </#if>

        <td align="right">
        <a href="toc.html">
            <span class="vector-font"><i class="fa fa-list vector-font" aria-hidden="true"></i></span>
            <span style="position:relative;top:-2px;">Contents</span>
        </a>
        </td>
    </tr>
</table>

<span id="copyright">
        <img src="img/eclipse_foundation_logo_tiny.png" height="20px" alt="Eclipse Foundation Logo" align="top"/>
        <span >Copyright &copy; 2017, 2024 Oracle and/or its affiliates. All rights reserved.</span>
</span>

</body>
</html>
