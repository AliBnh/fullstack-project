import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import api from '../services/api'
import toast from 'react-hot-toast'
import { FiX } from 'react-icons/fi'

export default function Favorites() {
  const [cars, setCars] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => { api.get('/api/favorites').then(r => { setCars(r.data); setLoading(false) }) }, [])

  const remove = async (e, carId) => {
    e.preventDefault()
    e.stopPropagation()
    try {
      await api.delete(`/api/favorites/${carId}`)
      setCars(prev => prev.filter(c => c.id !== carId))
      toast.success('Retiré des favoris')
    } catch (err) {
      toast.error('Erreur')
    }
  }

  if (loading) return <div className="text-gray-500 text-center mt-20 text-sm">Chargement...</div>

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">Mes favoris</h1>
        <p className="text-gray-500 text-sm mt-1">{cars.length} voiture{cars.length !== 1 ? 's' : ''} sauvegardée{cars.length !== 1 ? 's' : ''}</p>
      </div>

      {cars.length === 0 ? (
        <div className="text-center py-20 bg-white/5 border border-white/10 rounded-2xl">
          <p className="text-5xl mb-4">❤️</p>
          <p className="text-gray-400">Aucun favori pour le moment</p>
          <Link to="/" className="inline-block mt-4 text-blue-400 hover:underline text-sm">Explorer les voitures</Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {cars.map(car => (
            <div key={car.id} className="relative group bg-white/5 border border-white/10 rounded-xl overflow-hidden hover:border-white/20 transition">
              <Link to={`/cars/${car.id}`}>
                <div className="aspect-[16/10] overflow-hidden bg-white/5">
                  {car.imageUrl ? (
                    <img src={car.imageUrl} alt="" className="w-full h-full object-cover group-hover:scale-[1.03] transition-transform duration-300" />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-4xl opacity-30">🚗</div>
                  )}
                </div>
                <div className="p-4">
                  <h3 className="text-white font-medium text-sm">{car.marque} {car.modele}</h3>
                  <p className="text-gray-500 text-xs mt-0.5">{car.annee} • {car.localisation}</p>
                  <p className="text-blue-400 font-semibold text-sm mt-2">{car.prix?.toLocaleString()} MAD</p>
                </div>
              </Link>
              <button onClick={(e) => remove(e, car.id)}
                className="absolute top-3 right-3 w-8 h-8 bg-black/70 hover:bg-red-500 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition z-10 cursor-pointer">
                <FiX size={14} className="text-white" />
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
