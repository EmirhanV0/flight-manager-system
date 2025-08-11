# 🔧 Git Kurulumu ve Repository Setup

## Git Kurulumu

### Windows için Git Kurulumu

1. **Git'i indirin:**
   - https://git-scm.com/download/win adresinden Git for Windows'u indirin
   - Veya Chocolatey ile: `choco install git`

2. **Kurulum sonrası PowerShell'i yeniden başlatın**

3. **Git'in kurulu olduğunu doğrulayın:**
   ```powershell
   git --version
   ```

## Repository Setup

Git kurulduktan sonra aşağıdaki komutları çalıştırın:

### 1. Git Repository Başlatma
```powershell
# UYS dizinine gidin
cd C:\project\uys

# Git repository başlatın
git init

# Global git config (ilk kez kullanıyorsanız)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### 2. İlk Commit
```powershell
# Tüm dosyaları staging'e ekleyin
git add .

# İlk commit'i yapın
git commit -m "feat: initial project setup

- Project structure created
- Infrastructure setup with Docker Compose
- README.md with comprehensive documentation
- UYS Rule Set integration
- Development environment configuration"
```

### 3. Branch Yapısı Oluşturma
```powershell
# Dev branch oluşturun
git checkout -b dev

# Feature branch örneği
git checkout -b feature/reference-manager-setup

# Main branch'e geri dönün
git checkout main
```

### 4. GitHub Repository (Opsiyonel)

#### GitHub'da Repository Oluşturma
1. GitHub.com'a gidin
2. "New repository" butonuna tıklayın
3. Repository adı: `uys-project`
4. Description: "Unified Yield System - Havacılık Uçuş Yönetim Sistemi"
5. Public veya Private seçin
6. "Create repository" butonuna tıklayın

#### Remote Origin Ekleme
```powershell
# Remote origin ekleyin (URL'yi kendi repository'nizle değiştirin)
git remote add origin https://github.com/yourusername/uys-project.git

# Main branch'i push edin
git push -u origin main

# Dev branch'i push edin
git push -u origin dev
```

## Branch Stratejisi

### Ana Branch'ler
- **`main`**: Production branch
- **`dev`**: Development branch

### Feature Branch'ler
- **`feature/*`**: Yeni özellikler için
  - `feature/reference-manager-setup`
  - `feature/flight-service-setup`
  - `feature/archive-service-setup`
  - `feature/frontend-setup`

### Hotfix Branch'ler
- **`hotfix/*`**: Acil düzeltmeler için
  - `hotfix/security-patch`
  - `hotfix/critical-bug-fix`

### Release Branch'ler
- **`release/*`**: Release hazırlığı için
  - `release/v1.0.0`
  - `release/v1.1.0`

## Commit Mesaj Kuralları

```
feat: yeni özellik
fix: hata düzeltmesi
docs: dokümantasyon
test: test ekleme/düzenleme
refactor: kod refactoring
style: kod formatı
perf: performans iyileştirmesi
ci: CI/CD değişiklikleri
```

## Örnek Workflow

```powershell
# Yeni feature branch oluştur
git checkout dev
git pull origin dev
git checkout -b feature/new-feature

# Değişiklikleri yap ve commit et
git add .
git commit -m "feat: add new feature"

# Dev branch'e merge et
git checkout dev
git merge feature/new-feature

# Main branch'e merge et (release için)
git checkout main
git merge dev
git tag v1.0.0
git push origin main --tags
```

## Git Hooks (Opsiyonel)

### Pre-commit Hook
```bash
#!/bin/sh
# .git/hooks/pre-commit dosyasına ekleyin

# Code formatting kontrolü
mvn spotless:check

# Test çalıştırma
mvn test
```

### Commit-msg Hook
```bash
#!/bin/sh
# .git/hooks/commit-msg dosyasına ekleyin

# Commit mesaj formatı kontrolü
commit_regex='^(feat|fix|docs|test|refactor|style|perf|ci)(\(.+\))?: .+'
if ! grep -qE "$commit_regex" "$1"; then
    echo "ERROR: Commit message format is invalid"
    echo "Format: type(scope): description"
    exit 1
fi
```

## Troubleshooting

### Git Credentials
```powershell
# GitHub token kullanımı
git config --global credential.helper store
# GitHub token'ınızı girin
```

### Large Files
```powershell
# .gitignore'da büyük dosyaları kontrol edin
git ls-files --size | sort -n -r | head -10
```

### Branch Cleanup
```powershell
# Merged branch'leri sil
git branch --merged | grep -v "\*" | xargs -n 1 git branch -d
``` 