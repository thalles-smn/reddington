
# Reddington
Aplicação desenvolvida para demonstrar o monitoramento de aplicação com elasticsearch/kibana/logstash e sentry

## Requisitos

 - Java 8
 - Maven
 - Elasticsearch
 - Kibana
 - Logstash
 - MySQL
 - Postman
 
 ## Executando
 
 - Inicie o serviço do Elasticsearch: `bin/elasticsearch` ou caso Windows `bin\elasticsearch.bat`. O serviço é iniciado em `http://localhost:9200/`
 - Inicie o serviço do Kibana: `bin/kibana` ou caso Windows `bin\kibana.bat`. O serviço é iniciado em `http://localhost:5601/`
 - Abra a pasta de download do logstash/bin e crie um arquivo chamado logstash.conf com o seguinte conteudo: `input {
	tcp {
       port => 4560
       codec => json_lines
	}
} 
output {
	elasticsearch { 
		hosts => ["localhost:9200"] 
		index => "%{app}-%{+YYYY.MM.dd}"
  }  
  stdout { codec => rubydebug }
}`
 - Inicie o serviço do logstash: `bin/logstash -f logstash.conf` ou caso Windows `bin\logstash.bat -f logstash.conf`
 - Abra o diretório do projeto e execute `mvn clean install` e aguarde a aplicação iniciar
 - Abra o postman e importe a coleção existente na pasta postman do projeto e teste os endpoints

