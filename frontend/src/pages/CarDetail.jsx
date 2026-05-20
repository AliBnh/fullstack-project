import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'
import { FiHeart, FiTrash2, FiEdit, FiArrowLeft, FiPhone } from 'react-icons/fi'

export default function CarDetail() {
  const { id } = useParams()
  const { user } = useAuth()
  const navigate = useNavigate()
  const [car, setCar] = useState(null)
  const [isOwner, setIsOwner] = useState(false)
  const [isFav, setIsFav] = useState(false)

  useEffect(() => {
    api.get(`/api/cars/${id}`).then(r => {
      setCar(r.data)
      if (user) {
        api.get('/api/cars/my').then(my => {
          setIsOwner(my.data.content.some(c => c.id === r.data.id))
        }).catch(() => {})
        api.get(`/api/favorites/check/${id}`).then(f => setIsFav(f.data.favorited)).catch(() => {})
      }
    }).catch(() => navigate('/'))
  }, [id])

  const toggleFavorite = async () => {
    try {
      if (isFav) {
        await api.delete(`/api/favorites/${id}`)
        setIsFav(false)
        toast.success('Retiré des favoris')
      } else {
        await api.post(`/api/favorites/${id}`)
        setIsFav(true)
        toast.success('Ajouté aux favoris')
      }
    } catch { toast.error('Erreur') }
  }

  const handleDelete = async () => {
    if (!confirm('Supprimer cette annonce?')) return
    await api.delete(`/api/cars/${id}`)
    toast.success('Annonce supprimée')
    navigate('/')
  }

  if (!car) return <div className="text-gray-500 text-center mt-20 text-sm">Chargement...</div>

  return (
    <div className="max-w-4xl mx-auto">
      <button onClick={() => navigate(-1)} className="flex items-center gap-1.5 text-gray-500 hover:text-white text-sm mb-6 transition">
        <FiArrowLeft size={14} /> Retour
      </button>

      <div className="bg-white/5 border border-white/10 rounded-2xl overflow-hidden">
        <div className="aspect-[2/1] max-h-[400px] overflow-hidden">
          {car.imageUrl ? (
            <img src={car.imageUrl.startsWith('http') ? car.imageUrl : `${api.defaults.baseURL}${car.imageUrl}`} alt={`${car.marque} ${car.modele}`} className="w-full h-full object-cover" />
          ) : (
            <div className="w-full h-full bg-gradient-to-br from-gray-800 to-gray-900 flex flex-col items-center justify-center">
              <span className="text-6xl mb-2">🚗</span>
              <span className="text-sm text-gray-600">Aucune photo pour cette annonce</span>
            </div>
          )}
        </div>

        <div className="p-8">
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-2xl font-bold text-white">{car.marque} {car.modele}</h1>
              <p className="text-gray-500 text-sm mt-1">Publié par {car.sellerName} • {car.localisation}</p>
            </div>
            <div className="text-right">
              <p className="text-2xl font-bold text-blue-400">{car.prix?.toLocaleString()} MAD</p>
            </div>
          </div>

          {/* Seller contact */}
          {car.sellerPhone && (
            <div className="mt-4 flex items-center gap-3 p-4 bg-green-500/10 border border-green-500/20 rounded-xl">
              <FiPhone className="text-green-400" size={18} />
              <div>
                <p className="text-white text-sm font-medium">Contacter le vendeur</p>
                <a href={`tel:${car.sellerPhone}`} className="text-green-400 font-semibold text-lg hover:underline">{car.sellerPhone}</a>
              </div>
            </div>
          )}

          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mt-6">
            {[
              ['Année', car.annee],
              ['Kilométrage', `${car.kilometrage?.toLocaleString()} km`],
              ['Carburant', car.carburant],
              ['Transmission', car.transmission],
            ].map(([label, val]) => (
              <div key={label} className="bg-white/5 border border-white/10 p-3 rounded-xl text-center">
                <p className="text-[11px] text-gray-500 uppercase tracking-wide">{label}</p>
                <p className="text-white font-medium text-sm mt-1">{val || '—'}</p>
              </div>
            ))}
          </div>

          {car.couleur && (
            <div className="mt-6 flex items-center gap-2">
              <span className="text-gray-500 text-sm">Couleur:</span>
              <span className="text-white text-sm">{car.couleur}</span>
            </div>
          )}

          {car.description && (
            <div className="mt-6 p-4 bg-white/5 border border-white/10 rounded-xl">
              <p className="text-gray-300 text-sm leading-relaxed">{car.description}</p>
            </div>
          )}

          <div className="flex gap-3 mt-8 pt-6 border-t border-white/10">
            {user && (
              <button onClick={toggleFavorite}
                className={`flex items-center gap-2 px-4 py-2.5 rounded-lg text-sm transition ${isFav ? 'bg-red-500/20 text-red-400 hover:bg-red-500/30' : 'bg-white/10 text-gray-400 hover:bg-white/15 hover:text-white'}`}>
                <FiHeart size={14} fill={isFav ? 'currentColor' : 'none'} />
                {isFav ? 'Retirer des favoris' : 'Ajouter aux favoris'}
              </button>
            )}
            {isOwner && (
              <>
                <button onClick={() => navigate(`/edit/${car.id}`)} className="flex items-center gap-2 bg-blue-500/20 hover:bg-blue-500/30 text-blue-400 px-4 py-2.5 rounded-lg text-sm transition">
                  <FiEdit size={14} /> Modifier
                </button>
                <button onClick={handleDelete} className="flex items-center gap-2 bg-red-500/20 hover:bg-red-500/30 text-red-400 px-4 py-2.5 rounded-lg text-sm transition">
                  <FiTrash2 size={14} /> Supprimer
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
