@echo off

cd /D "%~dp0"
del /q *.jar >nul

echo Building...
cd /D ../
rmdir /s /q build\libs 2>nul
powershell -c "./gradlew buildAndCollect"

cd /D "%~dp0"
echo Moving files...
for /d %%d in (..\build\libs\*) do move "%%d\*.jar" . >nul

echo Done.
pause >nul