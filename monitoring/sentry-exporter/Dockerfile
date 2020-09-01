FROM node:lts as prepare

WORKDIR /src
COPY package.json package.json

RUN npm install 

FROM prepare as build

COPY . .

FROM node:lts as runtime

WORKDIR /app
COPY --from=build /src .

CMD [ "node", "index.js" ]