import { Routes, Route } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import { AuthProvider } from './context/AuthContext'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import Cars from './pages/Cars'
import CarDetail from './pages/CarDetail'
import CarForm from './pages/CarForm'
import Login from './pages/Login'
import Register from './pages/Register'
import Favorites from './pages/Favorites'
import MyListings from './pages/MyListings'
import AI from './pages/AI'

export default function App() {
  return (
    <AuthProvider>
      <div className="min-h-screen bg-[#0a0a0a] text-white">
        <Navbar />
        <main className="max-w-6xl mx-auto px-6 py-10">
          <Routes>
            <Route path="/" element={<Cars />} />
            <Route path="/cars/:id" element={<CarDetail />} />
            <Route path="/add" element={<ProtectedRoute><CarForm /></ProtectedRoute>} />
            <Route path="/edit/:id" element={<ProtectedRoute><CarForm /></ProtectedRoute>} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/favorites" element={<ProtectedRoute><Favorites /></ProtectedRoute>} />
            <Route path="/my-listings" element={<ProtectedRoute><MyListings /></ProtectedRoute>} />
            <Route path="/ai" element={<ProtectedRoute><AI /></ProtectedRoute>} />
          </Routes>
        </main>
        <Toaster position="top-center" toastOptions={{ style: { background: '#1a1a1a', color: '#fff', border: '1px solid rgba(255,255,255,0.1)', fontSize: '13px' } }} />
      </div>
    </AuthProvider>
  )
}
