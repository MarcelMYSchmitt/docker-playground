# Introduction

What is Sentry?   
https://sentry.io/welcome/

Application and error tracking tool for various programming languages. For Angular it can be used to collect all client side logs for analyzing errors and bugs better. In addtion you can give customers better support because you can understand problems better.

# How to setup

For Setup in Docker you can find everything here. For Kubernetes, please use the official helm chart.

## Docker

1. Generate secret key among all containers:   
   ```
   docker run --rm sentry config generate-secret-key`  
   #r*c(bb0q90!pwcm!s%57%d++=ej%(x2@d_s=gn911+i=#@)6i=
   ```

2. Upgrade Sentry and create root user:
   ```
   docker-compose up -d postgres
   docker-compose up -d redis
   docker-compose run sentry sentry upgrade 
   ```

## Kubernetes

Official Helm Chart: https://github.com/helm/charts/tree/master/stable/sentry