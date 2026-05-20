import { useState, useEffect } from 'react'
import api from '../services/api'
import CarCard from '../components/CarCard'
import { FiSearch } from 'react-icons/fi'

export default function Cars() {
  const [cars, setCars] = useState([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [filters, setFilters] = useState({ marque: '', localisation: '', carburant: '', prixMax: '' })

  useEffect(() => { fetchCars() }, [page, filters])

  const fetchCars = async () => {
    setLoading(true)
    const params = { page, size: 9 }
    if (filters.marque) params.marque = filters.marque
    if (filters.localisation) params.localisation = filters.localisation
    if (filters.carburant) params.carburant = filters.carburant
    if (filters.prixMax) params.prixMax = filters.prixMax
    const { data } = await api.get('/api/cars', { params })
    setCars(data.content)
    setTotal(data.totalPages)
    setLoading(false)
  }

  const set = (k) => (e) => { setFilters({ ...filters, [k]: e.target.value }); setPage(0) }

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-white">Explorer les voitures</h1>
        <p className="text-gray-500 mt-1">Trouvez votre prochaine voiture parmi nos annonces vérifiées</p>
      </div>

      <div className="flex flex-wrap gap-2 mb-8 p-4 bg-white/5 border border-white/10 rounded-xl">
        <div className="relative flex-1 min-w-[150px]">
          <FiSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500" size={14} />
          <input placeholder="Marque..." value={filters.marque} onChange={set('marque')}
            className="w-full pl-9 pr-3 py-2 bg-white/5 border border-white/10 text-white rounded-lg text-sm placeholder-gray-500 outline-none focus:border-blue-500/50 transition" />
        </div>
        <input placeholder="Ville..." value={filters.localisation} onChange={set('localisation')}
          className="flex-1 min-w-[120px] px-3 py-2 bg-white/5 border border-white/10 text-white rounded-lg text-sm placeholder-gray-500 outline-none focus:border-blue-500/50 transition" />
        <select value={filters.carburant} onChange={set('carburant')}
          className="px-3 py-2 bg-white/5 border border-white/10 text-white rounded-lg text-sm outline-none focus:border-blue-500/50 transition">
          <option value="" className="bg-gray-900">Carburant</option>
          <option value="ESSENCE" className="bg-gray-900">Essence</option>
          <option value="DIESEL" className="bg-gray-900">Diesel</option>
          <option value="HYBRIDE" className="bg-gray-900">Hybride</option>
          <option value="ELECTRIQUE" className="bg-gray-900">Électrique</option>
        </select>
        <input type="number" placeholder="Prix max" value={filters.prixMax} onChange={set('prixMax')}
          className="w-28 px-3 py-2 bg-white/5 border border-white/10 text-white rounded-lg text-sm placeholder-gray-500 outline-none focus:border-blue-500/50 transition" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {cars.map(car => <CarCard key={car.id} car={car} />)}
      </div>

      {!loading && cars.length === 0 && <p className="text-gray-600 text-center mt-16 text-sm">Aucune voiture trouvée</p>}
      {loading && <div className="flex justify-center py-20"><div className="w-6 h-6 border-2 border-white/20 border-t-blue-400 rounded-full animate-spin" /></div>}

      {total > 1 && (
        <div className="flex justify-center gap-1.5 mt-8">
          {Array.from({ length: total }, (_, i) => (
            <button key={i} onClick={() => setPage(i)}
              className={`w-8 h-8 rounded-lg text-sm transition ${i === page ? 'bg-blue-500 text-white' : 'bg-white/5 text-gray-400 hover:bg-white/10'}`}>
              {i + 1}
            </button>
          ))}
        </div>
      )}
    </div>
  )
}
