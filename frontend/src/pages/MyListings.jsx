import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import api from '../services/api'
import toast from 'react-hot-toast'
import { FiEdit, FiTrash2, FiPlusCircle } from 'react-icons/fi'

export default function MyListings() {
  const [cars, setCars] = useState([])
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    api.get('/api/cars/my?size=50').then(r => { setCars(r.data.content); setLoading(false) })
  }, [])

  const handleDelete = async (id) => {
    if (!confirm('Supprimer cette annonce?')) return
    await api.delete(`/api/cars/${id}`)
    setCars(cars.filter(c => c.id !== id))
    toast.success('Annonce supprimée')
  }

  if (loading) return <div className="text-gray-500 text-center mt-20 text-sm">Chargement...</div>

  return (
    <div>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl font-bold text-white">Mes annonces</h1>
          <p className="text-gray-500 text-sm mt-1">
            {cars.length > 0 ? `${cars.length} annonce${cars.length > 1 ? 's' : ''} publiée${cars.length > 1 ? 's' : ''}` : 'Vous n\'avez pas encore publié d\'annonce'}
          </p>
        </div>
        <Link to="/add" className="flex items-center gap-2 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2.5 rounded-lg text-sm font-medium transition">
          <FiPlusCircle size={14} /> Publier une annonce
        </Link>
      </div>

      {cars.length === 0 ? (
        <div className="text-center py-20 bg-white/5 border border-white/10 rounded-2xl">
          <p className="text-5xl mb-4">🚗</p>
          <p className="text-gray-400">Aucune annonce pour le moment</p>
          <p className="text-gray-600 text-sm mt-1">Publiez votre première voiture en quelques clics</p>
          <Link to="/add" className="inline-block mt-4 bg-white text-black px-5 py-2 rounded-lg text-sm font-medium hover:bg-gray-200 transition">
            Publier maintenant
          </Link>
        </div>
      ) : (
        <div className="space-y-3">
          {cars.map(car => (
            <div key={car.id} className="flex items-center gap-4 bg-white/5 border border-white/10 rounded-xl p-4 hover:bg-white/[0.07] transition">
              <div className="w-24 h-16 rounded-lg overflow-hidden bg-white/5 flex-shrink-0">
                {car.imageUrl ? (
                  <img src={car.imageUrl} alt="" className="w-full h-full object-cover" />
                ) : (
                  <div className="w-full h-full flex items-center justify-center text-xl opacity-30">🚗</div>
                )}
              </div>
              <div className="flex-1 min-w-0">
                <Link to={`/cars/${car.id}`} className="text-white font-medium text-sm hover:text-blue-400 transition">
                  {car.marque} {car.modele}
                </Link>
                <p className="text-gray-500 text-xs mt-0.5">{car.annee} • {car.kilometrage?.toLocaleString()} km • {car.localisation}</p>
              </div>
              <span className="text-blue-400 font-semibold text-sm whitespace-nowrap">{car.prix?.toLocaleString()} MAD</span>
              <div className="flex gap-2">
                <button onClick={() => navigate(`/edit/${car.id}`)}
                  className="p-2 rounded-lg bg-white/5 hover:bg-blue-500/20 text-gray-400 hover:text-blue-400 transition">
                  <FiEdit size={14} />
                </button>
                <button onClick={() => handleDelete(car.id)}
                  className="p-2 rounded-lg bg-white/5 hover:bg-red-500/20 text-gray-400 hover:text-red-400 transition">
                  <FiTrash2 size={14} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
