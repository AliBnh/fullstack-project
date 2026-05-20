import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import toast from 'react-hot-toast'

export default function Register() {
  const [form, setForm] = useState({ email: '', password: '', firstName: '', lastName: '', city: '', phone: '' })
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await register(form)
      toast.success('Compte créé!')
      navigate('/')
    } catch { toast.error('Erreur lors de l\'inscription') }
    setLoading(false)
  }

  return (
    <div className="min-h-[80vh] flex items-center justify-center">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <div className="w-10 h-10 bg-blue-500 rounded-xl flex items-center justify-center text-sm font-bold text-white mx-auto mb-4">A</div>
          <h1 className="text-xl font-semibold text-white">Créer un compte</h1>
          <p className="text-gray-500 text-sm mt-1">Rejoignez AutoMarket gratuitement</p>
        </div>
        <form onSubmit={handleSubmit} className="bg-white/5 border border-white/10 p-6 rounded-2xl">
          <div className="space-y-3">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-xs text-gray-400 mb-1 block">Prénom</label>
                <input value={form.firstName} onChange={set('firstName')}
                  className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" required />
              </div>
              <div>
                <label className="text-xs text-gray-400 mb-1 block">Nom</label>
                <input value={form.lastName} onChange={set('lastName')}
                  className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" required />
              </div>
            </div>
            <div>
              <label className="text-xs text-gray-400 mb-1 block">Email</label>
              <input type="email" value={form.email} onChange={set('email')}
                className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" required />
            </div>
            <div>
              <label className="text-xs text-gray-400 mb-1 block">Mot de passe</label>
              <input type="password" value={form.password} onChange={set('password')}
                className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" required />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-xs text-gray-400 mb-1 block">Ville</label>
                <input value={form.city} onChange={set('city')}
                  className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" />
              </div>
              <div>
                <label className="text-xs text-gray-400 mb-1 block">Téléphone</label>
                <input value={form.phone} onChange={set('phone')}
                  className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" />
              </div>
            </div>
          </div>
          <button type="submit" disabled={loading}
            className="w-full mt-6 bg-white text-black py-2.5 rounded-lg text-sm font-medium hover:bg-gray-200 transition disabled:opacity-50">
            {loading ? 'Création...' : 'Créer mon compte'}
          </button>
        </form>
        <p className="text-gray-500 text-xs text-center mt-4">
          Déjà un compte? <Link to="/login" className="text-blue-400 hover:underline">Se connecter</Link>
        </p>
      </div>
    </div>
  )
}
