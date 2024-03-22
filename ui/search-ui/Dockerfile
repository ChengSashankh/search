# STEP 1 build static website
FROM node:alpine as builder
RUN apk update && apk add --no-cache make git
# Create app directory
WORKDIR /app
# Create app/nginx directory and copy default.conf to it
WORKDIR /app/nginx
COPY nginx/conf.d/default.conf /app/nginx/
# Install app dependencies
COPY package.json package-lock.json /app/
RUN cd /app && npm set progress=false && npm install
# Copy project files into the docker image
COPY .  /app
RUN cd /app && npm run prod

# STEP 2 build a small nginx image with static website
FROM nginx:alpine
# Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*
# From 'builder' copy website to default nginx public folder
COPY --from=builder /app/dist/search-ui /usr/share/nginx/html
# From 'builder' copy default nginx configuration to the configuration conf.d folder of nginx
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf
#expose
EXPOSE 8890
CMD ["nginx", "-g", "daemon off;"]
