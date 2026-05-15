@echo off
cd /d "%~dp0"
start "LandingPage" /B python -m http.server 8080 -d dist
start http://localhost:8080