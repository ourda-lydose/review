name: Continous Deployment (CD)

on:
  push:
  pull_request:

jobs:
  build:
    name: Cloud Run Deployment
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@master

      - name: Setup GCP Service Account
        uses: google-github-actions/setup-gcloud@v0
        with:
          version: "latest"
          service_account_email: ${{ secrets.GCP_SA_EMAIL }}
          service_account_key: ${{ secrets.GCP_SA_KEY }}
          export_default_credentials: true

      - name: Configure Docker
        run: |
          gcloud auth configure-docker

      - name: Build
        run: |
          docker build -t gcr.io/${{ secrets.GCP_PROJECT_ID }}/review:latest .

      - name: Push
        run: |
          docker push gcr.io/${{ secrets.GCP_PROJECT_ID }}/review:latest

      - name: Deploy
        run: |
          gcloud run deploy review \
          --region europe-west1 \
          --image gcr.io/${{ secrets.GCP_PROJECT_ID }}/review \
          --platform managed \
          --allow-unauthenticated \
          --project ${{ secrets.GCP_PROJECT_ID }} \
          --set-env-vars=PG_HOST=${{ secrets.PG_HOST }} \
          --set-env-vars=PG_DB=${{ secrets.PG_USER }} \
          --set-env-vars=PG_USER=${{ secrets.PG_PASS }}