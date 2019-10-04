# 環境準備

## 安裝 Java 8 或更高的版本  
>需可正常顯示 `java -version` 結果  

## 安裝 Maven 工具  

* 手動安裝   
http://maven.apache.org/download.cgi  
* (macOS) 或使用套件管理工具 Homebrew  
`/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`  
`brew install maven`

>安裝完成後可正常顯示 `mvn -v` 結果  

## 安裝 Eclipse  

* 手動安裝  
https://www.eclipse.org/downloads/  
* (macOS) 或使用套件管理工具 Homebrew cask  
`brew tap caskroom/cask`  
`brew install brew-cask`  
`brew cask install eclipse-jee`  

## 安裝 Eclipse Marketplace Features  
### 搜尋關鍵字 STS
1. 安裝 Spring Tool 4 - for Spring Boot(STS 4)  
2. 安裝 Spring Tool 3 Add-On for Spring Tools 4  

## 使用 Archetype 產生 lab project

### Windows
1. `cmd RUN-archetype.bat`  
2. groupId: iisigroup.trainging
2. artifactId: lab1
3. `mvn -f lab1/pom.xml clean package`  
4. `java -jar -Dserver.port=8080 lab1/target/lab1-5.0.1.jar`  

### macOS
1. `sh ./RUN-archetype.sh`  
2. groupId: iisigroup.trainging
2. artifactId: lab1
3. `mvn -f lab1/pom.xml clean package`  
4. `java -jar -Dserver.port=8080 lab1/target/lab1-5.0.1.jar`  

## 開啟瀏覽器或執行指令，測試啟動好的 application

### Windows
* `open http://localhost:8080/customers`  
* `open http://localhost:8080/swagger-ui.html`  

### macOS
* `curl http://localhost:8080/customers`  
* `curl http://localhost:8080/swagger-ui.html`  