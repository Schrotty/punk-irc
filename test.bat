@echo off
if [%1]==[] (
    echo No path specified!
) else (
    @echo on
    :: Maven clean-up & package
    call mvn clean
    call mvn package

    :: perform tests
    cd src/test
    python -m pytest --json=../../target/test/report.json --html=../../target/test/report.html --path=%1
    python grade.py --tests-file ./alltests --report-file ../../target/test/report.json > ../../target/test/grade.txt

    :: open reports
    start "" %~dp0\target\test\report.html
    start "" %~dp0\target\test\grade.txt

    :: back to root
    cd ../../
)