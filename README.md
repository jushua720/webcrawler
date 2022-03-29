# WebCrawler
Простой интернет crawler, который достает из страниц информацию о названии сайта. 


### Пример запроса
> curl -i -X POST -H 'Content-Type: application/json' -d '{"url": ["stackoverflow.com", "https<nolink>://youtube.com"]}' http<nolink>://localhost:8080/crawler
