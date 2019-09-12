/*<![CDATA[*/
function toPage(data){
    var scheduleId = $("#scheduleId").val();
    window.location.href="/taskLog?scheduleId="+scheduleId+"&page="+data;
}
/*]]>*/