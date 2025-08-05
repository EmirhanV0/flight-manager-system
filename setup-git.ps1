# Git Setup Script for UYS Project
# Bu script Git kurulumu sonrası çalıştırılmalıdır

Write-Host "🔧 UYS Project Git Setup" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green

# Git'in kurulu olup olmadığını kontrol et
try {
    $gitVersion = git --version
    Write-Host "✅ Git kurulu: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Git kurulu değil. Lütfen önce Git'i kurun." -ForegroundColor Red
    Write-Host "📥 Git'i şu adresten indirebilirsiniz: https://git-scm.com/download/win" -ForegroundColor Yellow
    exit 1
}

# Mevcut dizini kontrol et
$currentDir = Get-Location
Write-Host "📁 Mevcut dizin: $currentDir" -ForegroundColor Cyan

# Git repository başlat
if (-not (Test-Path ".git")) {
    Write-Host "🚀 Git repository başlatılıyor..." -ForegroundColor Yellow
    git init
    Write-Host "✅ Git repository başlatıldı" -ForegroundColor Green
} else {
    Write-Host "✅ Git repository zaten mevcut" -ForegroundColor Green
}

# Git config kontrol et
$userName = git config --global user.name
$userEmail = git config --global user.email

if (-not $userName -or -not $userEmail) {
    Write-Host "⚠️  Git config ayarlanmamış" -ForegroundColor Yellow
    Write-Host "Lütfen aşağıdaki komutları çalıştırın:" -ForegroundColor Cyan
    Write-Host "git config --global user.name 'Your Name'" -ForegroundColor White
    Write-Host "git config --global user.email 'your.email@example.com'" -ForegroundColor White
} else {
    Write-Host "✅ Git config ayarlı: $userName <$userEmail>" -ForegroundColor Green
}

# Dosyaları staging'e ekle
Write-Host "📦 Dosyalar staging'e ekleniyor..." -ForegroundColor Yellow
git add .

# Commit yap
Write-Host "💾 İlk commit yapılıyor..." -ForegroundColor Yellow
$commitMessage = @"
feat: initial project setup

- Project structure created
- Infrastructure setup with Docker Compose
- README.md with comprehensive documentation
- UYS Rule Set integration
- Development environment configuration
- Git setup and branch strategy
"@

git commit -m $commitMessage

# Branch yapısını oluştur
Write-Host "🌿 Branch yapısı oluşturuluyor..." -ForegroundColor Yellow

# Dev branch oluştur
git checkout -b dev
Write-Host "✅ dev branch oluşturuldu" -ForegroundColor Green

# Feature branch örnekleri oluştur
$featureBranches = @(
    "feature/reference-manager-setup",
    "feature/flight-service-setup", 
    "feature/archive-service-setup",
    "feature/frontend-setup"
)

foreach ($branch in $featureBranches) {
    git checkout -b $branch
    Write-Host "✅ $branch branch oluşturuldu" -ForegroundColor Green
    git checkout dev
}

# Main branch'e geri dön
git checkout main
Write-Host "✅ main branch'e dönüldü" -ForegroundColor Green

# Branch listesini göster
Write-Host "📋 Mevcut branch'ler:" -ForegroundColor Cyan
git branch -a

# Sonuç
Write-Host ""
Write-Host "🎉 Git setup tamamlandı!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host "📝 Sonraki adımlar:" -ForegroundColor Yellow
Write-Host "1. GitHub'da repository oluşturun" -ForegroundColor White
Write-Host "2. Remote origin ekleyin: git remote add origin <repository-url>" -ForegroundColor White
Write-Host "3. Push edin: git push -u origin main" -ForegroundColor White
Write-Host "4. Dev branch'i push edin: git push -u origin dev" -ForegroundColor White
Write-Host ""
Write-Host "📚 Detaylı bilgi için setup-git.md dosyasını inceleyin" -ForegroundColor Cyan 