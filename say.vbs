dim speechobject
set speechobject=createobject("sapi.spvoice")
speechobject.speak WScript.Arguments(0)