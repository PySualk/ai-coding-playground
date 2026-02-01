# Development stage for Tilt
FROM node:22-alpine AS development

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Expose Ionic dev server port
EXPOSE 4200

# Start the Ionic development server
CMD ["npx", "ionic", "serve", "--external", "--no-open", "--poll=2000"]

# Build stage
FROM node:22-alpine AS build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Production stage
FROM nginx:alpine AS production

# Copy built files from build stage (project renamed from 'frontend' to 'app')
COPY --from=build /app/dist/app/browser /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
