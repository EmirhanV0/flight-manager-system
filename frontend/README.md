# 🎨 Frontend (Vue 3 + Element Plus)

## 📋 Açıklama

UYS sisteminin Vue 3 ve Element Plus ile geliştirilmiş frontend uygulaması. Referans veri yönetimi ve uçuş operasyonları için modern web arayüzü sağlar.

## 🚀 Hızlı Başlangıç

### Gereksinimler
- Node.js 18+
- npm veya yarn
- Modern web browser

### Kurulum

1. **Bağımlılıkları yükleyin:**
```bash
npm install
```

2. **Development server'ı başlatın:**
```bash
npm run dev
```

3. **Production build:**
```bash
npm run build
```

## 📊 Uygulama Endpoint'leri

### Development
- **URL:** http://localhost:3000

### Production
- **URL:** http://localhost:3000 (build sonrası)

## 🛠️ Teknolojiler

### Core
- **Vue 3** - Progressive JavaScript framework
- **Vite** - Build tool ve dev server
- **Element Plus** - Vue 3 UI component library

### State Management
- **Pinia** - Vue 3 state management
- **Vue Router** - Client-side routing

### HTTP Client
- **Axios** - HTTP client
- **Interceptors** - Request/response handling

### Testing
- **Cypress** - E2E testing
- **Vitest** - Unit testing

## 📁 Proje Yapısı

```
frontend/
├── src/
│   ├── components/     # Vue bileşenleri
│   ├── views/         # Sayfa bileşenleri
│   ├── stores/        # Pinia stores
│   ├── services/      # API servisleri
│   ├── utils/         # Yardımcı fonksiyonlar
│   ├── assets/        # Statik dosyalar
│   └── router/        # Vue Router konfigürasyonu
├── public/            # Public dosyalar
├── cypress/           # E2E testler
└── tests/             # Unit testler
```

## 🎯 Özellikler

### Referans Veri Yönetimi
- ✅ Havayolu şirketi CRUD
- ✅ Uçak CRUD
- ✅ İstasyon CRUD
- ✅ Redis cache entegrasyonu
- ✅ Real-time güncellemeler

### Uçuş Operasyonları
- ✅ Uçuş oluşturma
- ✅ Uçuş güncelleme
- ✅ CSV dosya yükleme
- ✅ Referans veri doğrulama
- ✅ Form validation

### Kullanıcı Deneyimi
- ✅ Responsive design
- ✅ Loading states
- ✅ Error handling
- ✅ Toast notifications
- ✅ Keyboard shortcuts (Ctrl+S)

## 🧪 Test

### Unit Tests
```bash
npm run test:unit
```

### E2E Tests
```bash
npm run test:e2e
```

### Test Coverage
```bash
npm run test:coverage
```

## 🔧 Konfigürasyon

### Environment Variables
```env
VITE_API_BASE_URL=http://localhost:8081
VITE_FLIGHT_API_URL=http://localhost:8082
VITE_ARCHIVE_API_URL=http://localhost:8083
VITE_APP_TITLE=UYS - Unified Yield System
```

### Vite Config
```javascript
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
})
```

## 📝 API Entegrasyonu

### Axios Instance
```javascript
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor
api.interceptors.request.use(
  (config) => {
    // Add auth token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle errors
    if (error.response?.status === 401) {
      // Redirect to login
    }
    return Promise.reject(error)
  }
)
```

## 🚨 Troubleshooting

### Yaygın Sorunlar

1. **API bağlantı hatası:**
   - Backend servislerinin çalıştığını kontrol edin
   - CORS ayarlarını kontrol edin

2. **Build hatası:**
   - Node.js versiyonunu kontrol edin
   - node_modules'ü silip yeniden yükleyin

3. **Hot reload çalışmıyor:**
   - Vite dev server'ı yeniden başlatın
   - Port çakışmasını kontrol edin

## 📚 Daha Fazla Bilgi

- [Vue 3 Documentation](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [Vite](https://vitejs.dev/)
- [Pinia](https://pinia.vuejs.org/)
- [Cypress](https://cypress.io/) 