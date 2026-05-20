import { useState, useEffect, useRef } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../services/api'
import toast from 'react-hot-toast'
import { FiUpload, FiX } from 'react-icons/fi'

const empty = { marque: '', modele: '', annee: '', prix: '', kilometrage: '', carburant: '', transmission: '', couleur: '', description: '', localisation: '', imageUrl: '' }

export default function CarForm() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [form, setForm] = useState(empty)
  const [preview, setPreview] = useState(null)
  const [uploading, setUploading] = useState(false)
  const fileRef = useRef()

  useEffect(() => {
    if (id) {
      api.get(`/api/cars/${id}`).then(r => {
        const c = r.data
        setForm({ marque: c.marque || '', modele: c.modele || '', annee: c.annee || '', prix: c.prix || '', kilometrage: c.kilometrage || '', carburant: c.carburant || '', transmission: c.transmission || '', couleur: c.couleur || '', description: c.description || '', localisation: c.localisation || '', imageUrl: c.imageUrl || '' })
        if (c.imageUrl) setPreview(c.imageUrl.startsWith('http') ? c.imageUrl : `${api.defaults.baseURL}${c.imageUrl}`)
      })
    }
  }, [id])

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value })

  const handleFileChange = async (e) => {
    const file = e.target.files[0]
    if (!file) return
    setPreview(URL.createObjectURL(file))
    setUploading(true)
    try {
      const formData = new FormData()
      formData.append('file', file)
      const { data } = await api.post('/api/uploads', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
      setForm(prev => ({ ...prev, imageUrl: data.url }))
      toast.success('Image uploadée')
    } catch { toast.error('Erreur upload') }
    setUploading(false)
  }

  const removeImage = () => { setPreview(null); setForm({ ...form, imageUrl: '' }); if (fileRef.current) fileRef.current.value = '' }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const payload = { ...form, annee: Number(form.annee), prix: Number(form.prix), kilometrage: Number(form.kilometrage) || 0 }
    try {
      if (id) { await api.put(`/api/cars/${id}`, payload); toast.success('Annonce modifiée') }
      else { await api.post('/api/cars', payload); toast.success('Annonce publiée!') }
      navigate('/my-listings')
    } catch { toast.error('Erreur lors de la sauvegarde') }
  }

  const input = (label, key, type = 'text', required = true) => (
    <div key={key}>
      <label className="text-xs text-gray-400 mb-1 block">{label}</label>
      <input type={type} value={form[key]} onChange={set(key)} required={required}
        className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition" />
    </div>
  )

  return (
    <div className="max-w-2xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">{id ? 'Modifier l\'annonce' : 'Publier une voiture'}</h1>
        <p className="text-gray-500 text-sm mt-1">{id ? 'Mettez à jour les informations' : 'Remplissez les détails de votre véhicule'}</p>
      </div>

      <form onSubmit={handleSubmit} className="bg-white/5 border border-white/10 p-6 rounded-2xl">
        {/* Image upload */}
        <div className="mb-6">
          <label className="text-xs text-gray-400 mb-2 block">Photo du véhicule</label>
          {preview ? (
            <div className="relative w-full h-48 rounded-xl overflow-hidden">
              <img src={preview} alt="Preview" className="w-full h-full object-cover" />
              <button type="button" onClick={removeImage}
                className="absolute top-2 right-2 w-7 h-7 bg-black/70 hover:bg-red-500 rounded-full flex items-center justify-center transition">
                <FiX size={12} className="text-white" />
              </button>
            </div>
          ) : (
            <div onClick={() => fileRef.current?.click()}
              className="w-full h-48 border-2 border-dashed border-white/10 rounded-xl flex flex-col items-center justify-center cursor-pointer hover:border-blue-500/50 transition">
              <FiUpload size={24} className="text-gray-500 mb-2" />
              <p className="text-gray-500 text-sm">Cliquez pour ajouter une photo</p>
              <p className="text-gray-600 text-xs mt-1">JPG, PNG • Max 5MB</p>
            </div>
          )}
          <input ref={fileRef} type="file" accept="image/*" onChange={handleFileChange} className="hidden" />
          {uploading && <p className="text-blue-400 text-xs mt-2">Upload en cours...</p>}
        </div>

        <div className="grid grid-cols-2 gap-4">
          {input('Marque', 'marque')}
          {input('Modèle', 'modele')}
          {input('Année', 'annee', 'number')}
          {input('Prix (MAD)', 'prix', 'number')}
          {input('Kilométrage', 'kilometrage', 'number', false)}
          {input('Couleur', 'couleur', 'text', false)}
          {input('Ville', 'localisation')}
          <div>
            <label className="text-xs text-gray-400 mb-1 block">Carburant</label>
            <select value={form.carburant} onChange={set('carburant')}
              className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition">
              <option value="" className="bg-gray-900">—</option>
              <option value="ESSENCE" className="bg-gray-900">Essence</option>
              <option value="DIESEL" className="bg-gray-900">Diesel</option>
              <option value="HYBRIDE" className="bg-gray-900">Hybride</option>
              <option value="ELECTRIQUE" className="bg-gray-900">Électrique</option>
            </select>
          </div>
          <div>
            <label className="text-xs text-gray-400 mb-1 block">Transmission</label>
            <select value={form.transmission} onChange={set('transmission')}
              className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition">
              <option value="" className="bg-gray-900">—</option>
              <option value="MANUELLE" className="bg-gray-900">Manuelle</option>
              <option value="AUTOMATIQUE" className="bg-gray-900">Automatique</option>
            </select>
          </div>
        </div>
        <div className="mt-4">
          <label className="text-xs text-gray-400 mb-1 block">Description</label>
          <textarea value={form.description} onChange={set('description')} rows={3}
            className="w-full px-3 py-2.5 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition resize-none" />
        </div>
        <button type="submit" className="w-full mt-6 bg-white text-black py-2.5 rounded-lg text-sm font-medium hover:bg-gray-200 transition">
          {id ? 'Enregistrer les modifications' : 'Publier l\'annonce'}
        </button>
      </form>
    </div>
  )
}
