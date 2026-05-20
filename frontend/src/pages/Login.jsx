import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import toast from 'react-hot-toast'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await login(email, password)
      toast.success('Bienvenue!')
      navigate('/')
    } catch { toast.error('Email ou mot de passe incorrect') }
    setLoading(false)
  }

  return (
    <div className="min-h-[80vh] flex items-center justify-center">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <div className="w-10 h-10 bg-blue-500 rounded-xl flex items-center justify-center text-sm font-bold text-white mx-auto mb-4">A</div>
          <h1 className="text-xl font-semibold text-white">Connexion à AutoMarket</h1>
          <p className="text-gray-500 text-sm mt-1">Accédez à votre compte</p>
        </div>
        <form onSubmit={handleSubmit} className="bg-white/5 border border-white/10 p-6 rounded-2xl">
          <div className="space-y-4">
            <div>
              <label className="text-xs text-gray-400 mb-1 block">Email</label>
              <input type="email" value={email} onChange={e => setEmail(e.target.value)}
                className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" required />
            </div>
            <div>
              <label className="text-xs text-gray-400 mb-1 block">Mot de passe</label>
              <input type="password" value={password} onChange={e => setPassword(e.target.value)}
                className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" required />
            </div>
          </div>
          <button type="submit" disabled={loading}
            className="w-full mt-6 bg-white text-black py-2.5 rounded-lg text-sm font-medium hover:bg-gray-200 transition disabled:opacity-50">
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>
        <p className="text-gray-500 text-xs text-center mt-4">
          Pas encore de compte? <Link to="/register" className="text-blue-400 hover:underline">Créer un compte</Link>
        </p>
      </div>
    </div>
  )
}
