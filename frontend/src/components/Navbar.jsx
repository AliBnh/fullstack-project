import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { FiLogOut, FiHeart, FiPlusCircle, FiList, FiZap } from 'react-icons/fi'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const { pathname } = useLocation()

  const handleLogout = () => { logout(); navigate('/login') }

  const navLink = (to, label, icon) => (
    <Link to={to} className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm transition
      ${pathname === to ? 'bg-white/10 text-white' : 'text-gray-400 hover:text-white hover:bg-white/5'}`}>
      {icon}{label}
    </Link>
  )

  return (
    <nav className="border-b border-white/10 backdrop-blur-md bg-black/50 sticky top-0 z-50">
      <div className="max-w-6xl mx-auto px-6 h-14 flex items-center justify-between">
        <Link to="/" className="flex items-center gap-2">
          <div className="w-7 h-7 bg-blue-500 rounded-lg flex items-center justify-center text-xs font-bold text-white">A</div>
          <span className="font-semibold text-white text-sm">AutoMarket</span>
        </Link>

        <div className="flex items-center gap-1">
          {navLink('/', 'Explorer', null)}
          {user && (
            <>
              {navLink('/my-listings', 'Mes annonces', <FiList size={14} />)}
              {navLink('/favorites', 'Favoris', <FiHeart size={14} />)}
              {navLink('/add', 'Publier', <FiPlusCircle size={14} />)}
              {navLink('/ai', 'AI', <FiZap size={14} />)}
            </>
          )}
        </div>

        <div className="flex items-center gap-3">
          {user ? (
            <>
              <span className="text-xs text-gray-500">{user.firstName}</span>
              <button onClick={handleLogout} className="text-gray-500 hover:text-white transition"><FiLogOut size={16} /></button>
            </>
          ) : (
            <Link to="/login" className="bg-white text-black px-4 py-1.5 rounded-md text-sm font-medium hover:bg-gray-200 transition">
              Se connecter
            </Link>
          )}
        </div>
      </div>
    </nav>
  )
}
