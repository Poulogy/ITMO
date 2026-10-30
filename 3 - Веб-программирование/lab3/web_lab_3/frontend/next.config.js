
/** @type {import('next').NextConfig} */
const nextConfig = {

    trailingSlash: false,
    images: {
        unoptimized: true
    },
    async rewrites() {
        return [
            {
                source: '/api/AuthService',
                destination: 'http://backend:8080/backend/AuthService',
            },
            {
                source: '/api/PointService',
                destination: 'http://backend:8080/backend/PointService',
            },
        ]
    }
}

module.exports = nextConfig