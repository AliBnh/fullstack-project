import { Link } from 'react-router-dom'

export default function CarCard({ car }) {
  const imgSrc = car.imageUrl
    ? (car.imageUrl.startsWith('http') ? car.imageUrl : `${import.meta.env.VITE_API_URL || ''}${car.imageUrl}`)
    : null

  return (
    <Link to={`/cars/${car.id}`} className="group block bg-white/5 border border-white/10 rounded-xl overflow-hidden hover:border-white/20 hover:bg-white/[0.07] transition-all duration-200">
      <div className="aspect-[16/10] overflow-hidden">
        {imgSrc ? (
          <img src={imgSrc} alt={`${car.marque} ${car.modele}`}
            className="w-full h-full object-cover group-hover:scale-[1.03] transition-transform duration-300" />
        ) : (
          <div className="w-full h-full bg-gradient-to-br from-gray-800 to-gray-900 flex flex-col items-center justify-center">
            <span className="text-3xl mb-1">🚗</span>
            <span className="text-[10px] text-gray-600">Photo non disponible</span>
          </div>
        )}
      </div>
      <div className="p-4">
        <div className="flex justify-between items-start">
          <div>
            <h3 className="text-white font-medium text-[15px]">{car.marque} {car.modele}</h3>
            <p className="text-gray-500 text-xs mt-0.5">{car.annee} • {car.kilometrage?.toLocaleString()} km • {car.localisation}</p>
          </div>
        </div>
        <div className="flex items-center justify-between mt-3">
          <span className="text-blue-400 font-semibold">{car.prix?.toLocaleString()} MAD</span>
          <div className="flex gap-1.5">
            {car.carburant && <span className="text-[10px] px-2 py-0.5 rounded-full bg-white/10 text-gray-400">{car.carburant}</span>}
            {car.transmission && <span className="text-[10px] px-2 py-0.5 rounded-full bg-white/10 text-gray-400">{car.transmission}</span>}
          </div>
        </div>
      </div>
    </Link>
  )
}
