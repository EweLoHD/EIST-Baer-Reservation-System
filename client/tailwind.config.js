module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
    "./node_modules/flowbite/**/*.js"
  ],
  theme: {
    extend: {
      spacing: {
        '128': '32rem',
        '192': '48rem',
        '224': '56rem',
        '256': '64rem'
      }
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
  darkMode: 'class',
}