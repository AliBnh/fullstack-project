import { useState } from 'react'
import api from '../services/api'
import toast from 'react-hot-toast'
import { FiZap } from 'react-icons/fi'

export default function AI() {
  const [tab, setTab] = useState('chat')
  const [message, setMessage] = useState('')
  const [response, setResponse] = useState('')
  const [loading, setLoading] = useState(false)
  const [priceForm, setPriceForm] = useState({ marque: '', modele: '', annee: '', kilometrage: '', carburant: '', transmission: '', localisation: '' })

  const handleChat = async (e) => {
    e.preventDefault(); setLoading(true); setResponse('')
    try { const { data } = await api.post('/api/ai/chat', { message }); setResponse(data.response) }
    catch { toast.error('Erreur AI') }
    setLoading(false)
  }

  const handlePrice = async (e) => {
    e.preventDefault(); setLoading(true); setResponse('')
    try { const { data } = await api.post('/api/ai/estimate-price', { ...priceForm, annee: Number(priceForm.annee), kilometrage: Number(priceForm.kilometrage) }); setResponse(data.response) }
    catch { toast.error('Erreur AI') }
    setLoading(false)
  }

  const handleDesc = async (e) => {
    e.preventDefault(); setLoading(true); setResponse('')
    try { const { data } = await api.post('/api/ai/generate-description', { ...priceForm, annee: Number(priceForm.annee), kilometrage: Number(priceForm.kilometrage) }); setResponse(data.response) }
    catch { toast.error('Erreur AI') }
    setLoading(false)
  }

  const setP = (k) => (e) => setPriceForm({ ...priceForm, [k]: e.target.value })
  const tabs = [['chat', '💬 Chat'], ['price', '💰 Estimation prix'], ['desc', '📝 Générer description']]

  const inputCls = "w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition placeholder-gray-600"

  return (
    <div className="max-w-3xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white flex items-center gap-2"><FiZap className="text-blue-400" /> Assistant AI</h1>
        <p className="text-gray-500 text-sm mt-1">Propulsé par Groq (LLaMA 3.3 70B)</p>
      </div>

      <div className="flex gap-1 mb-6 p-1 bg-white/5 border border-white/10 rounded-xl w-fit">
        {tabs.map(([k, label]) => (
          <button key={k} onClick={() => { setTab(k); setResponse('') }}
            className={`px-4 py-2 rounded-lg text-sm transition ${tab === k ? 'bg-white/10 text-white' : 'text-gray-500 hover:text-white'}`}>{label}</button>
        ))}
      </div>

      {tab === 'chat' && (
        <form onSubmit={handleChat} className="bg-white/5 border border-white/10 p-6 rounded-2xl">
          <textarea value={message} onChange={e => setMessage(e.target.value)} rows={3} placeholder="Posez votre question sur les voitures, le marché marocain, l'entretien..."
            className={inputCls + " resize-none"} required />
          <button type="submit" disabled={loading} className="mt-4 bg-blue-500 hover:bg-blue-600 text-white px-5 py-2.5 rounded-lg text-sm font-medium transition disabled:opacity-50">
            {loading ? '⏳ Réflexion...' : 'Envoyer'}
          </button>
        </form>
      )}

      {(tab === 'price' || tab === 'desc') && (
        <form onSubmit={tab === 'price' ? handlePrice : handleDesc} className="bg-white/5 border border-white/10 p-6 rounded-2xl">
          <div className="grid grid-cols-2 gap-3">
            <input placeholder="Marque" value={priceForm.marque} onChange={setP('marque')} className={inputCls} required />
            <input placeholder="Modèle" value={priceForm.modele} onChange={setP('modele')} className={inputCls} required />
            <input type="number" placeholder="Année" value={priceForm.annee} onChange={setP('annee')} className={inputCls} required />
            <input type="number" placeholder="Kilométrage" value={priceForm.kilometrage} onChange={setP('kilometrage')} className={inputCls} required />
            <select value={priceForm.carburant} onChange={setP('carburant')} className={inputCls}>
              <option value="" className="bg-gray-900">Carburant</option><option value="ESSENCE" className="bg-gray-900">Essence</option>
              <option value="DIESEL" className="bg-gray-900">Diesel</option><option value="HYBRIDE" className="bg-gray-900">Hybride</option>
            </select>
            <select value={priceForm.transmission} onChange={setP('transmission')} className={inputCls}>
              <option value="" className="bg-gray-900">Transmission</option><option value="MANUELLE" className="bg-gray-900">Manuelle</option>
              <option value="AUTOMATIQUE" className="bg-gray-900">Automatique</option>
            </select>
            <input placeholder="Ville" value={priceForm.localisation} onChange={setP('localisation')} className={inputCls + " col-span-2"} />
          </div>
          <button type="submit" disabled={loading} className="mt-4 bg-blue-500 hover:bg-blue-600 text-white px-5 py-2.5 rounded-lg text-sm font-medium transition disabled:opacity-50">
            {loading ? '⏳ Analyse...' : tab === 'price' ? 'Estimer le prix' : 'Générer la description'}
          </button>
        </form>
      )}

      {response && (
        <div className="mt-6 bg-white/5 border border-blue-500/20 p-6 rounded-2xl">
          <p className="text-blue-400 text-xs font-medium uppercase tracking-wide mb-3">🤖 Réponse AI</p>
          <pre className="text-gray-300 whitespace-pre-wrap text-sm leading-relaxed font-sans">{response}</pre>
        </div>
      )}
    </div>
  )
}
