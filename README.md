# MULTI TEAM

## Instruções para trabalhar com o Docker neste projeto

---
#### Na diretório que contém o arquivo Dockerfile execute o comando abaixo. O mesmo cria a imagem da aplicação respeitando as regras preestabelecidas no arquivo Dockerfile.

Gerar a imagem com o nome app_staging:
```
docker build -t app_staging .
```
 Execute o comando abaixo para gerar o container a partir da imagem criada no docker build:
```
docker run -d -p 8080:8080 -e SENDGRID_API_KEY=[KEY_SECRET] -e SENDGRID_EMAIL_FROM=[MY_EMAIL] -e SPRING_PROFILES_ACTIVE=[staging] --name=app_staging staging
```
Para gerar o container do postgres a partir de uma imagem execute o seguiente comando:
```
docker run -d --name=postgres_staging -p 5432:5432 -e "POSTGRES_USER=postgres" -e "POSTGRES_PASSWORD=postgres" -e "POSTGRES_DB=staging" -v /home/mydir/Documents/postgres_container_data:/var/lib/postgresql/data postgres:alpine3.18
```

## Para visualizar a cobertura de código: Jacoco - Code Covearge
```
Acesse o diretório 
/target/site/index.html
```
## Desenvolvimento seguro neste projeto

Esse projeto utiliza a plataforma HoruSec para análise do código estático, isso 
significa que a análise de segurança no código fonte segue as diretrizes definidas
pela HoruSec.

Para executar a análise de segurança do código acessar o diretório raiz do projeto
e executar o comando abaixo: A opção "-D true" desabilita o uso do Docker.
```
horusec start -D true
```