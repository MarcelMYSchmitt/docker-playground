FROM node:lts as prepare

WORKDIR /src
COPY package.json package-lock.json ./

RUN npm install 

FROM prepare as build

COPY . /src
RUN npm run build-release

FROM nginx:1.13-alpine as runtime

# get lua prometheus metric functionality for nginx
RUN apk add --no-cache nginx-mod-http-lua && apk update && apk add ca-certificates && update-ca-certificates
RUN mkdir -p /etc/nginx/nginx-lua-prometheus && cd /etc/nginx/nginx-lua-prometheus
COPY nginx/prometheus.lua /etc/nginx/nginx-lua-prometheus/prometheus.lua

RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /src/dist /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/nginx.conf

RUN chmod 777 -R /usr/share/nginx/html
RUN mkdir -p /run/nginx

CMD ["nginx", "-g", "daemon off;"]
