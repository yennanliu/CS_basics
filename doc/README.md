# Documentation Site

This directory contains documentation that is automatically deployed to GitHub Pages.

## How It Works

1. The content in this `doc` directory is automatically deployed to GitHub Pages whenever changes are pushed to the main branch.
2. The deployment is handled by GitHub Actions using the workflow defined in `.github/workflows/deploy-docs.yml`.
3. The site is accessible at `https://[username].github.io/CS_basics/` after deployment.

## Local Development

To test the documentation site locally:

1. Navigate to the `doc` directory
2. Serve the content with a simple HTTP server:
   ```
   python -m http.server
   ```
3. Open your browser to `http://localhost:8000` to view the site

## Adding New Documentation

1. Add your markdown or HTML files to the appropriate subdirectory
2. Update the `index.html` file to include links to your new documentation
3. Commit and push your changes to the main branch to trigger deployment

## File Types Supported

- Markdown (.md) files (viewed as rendered HTML on GitHub Pages)
- HTML files
- PDF files
- Images and other assets 