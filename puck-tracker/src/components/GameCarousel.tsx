import { Game, NhlTeam } from '../types';

interface GameCarouselProps {
  games: Game[];
  selectedGame: Game | null;
  selectedTeam: NhlTeam | null;
  onGameSelect: (game: Game) => void;
  teamColor: string;
  accentColor: string;
}

function proxyUrl(url: string): string {
  if (!url) return '';
  return `https://corsproxy.io/?url=${encodeURIComponent(url)}`;
}

export default function GameCarousel({
  games,
  selectedGame,
  selectedTeam,
  onGameSelect,
  teamColor,
  accentColor,
}: GameCarouselProps) {
  return (
    <div className="game-carousel-section">
      <p className="section-label">Select a Matchup:</p>
      <div className="game-carousel">
        {games.map((game) => {
          const isSelected = selectedGame?.id === game.id;
          const selectedTeamWon =
            (game.homeTeam === selectedTeam?.abbrev && game.homeScore > game.awayScore) ||
            (game.awayTeam === selectedTeam?.abbrev && game.awayScore > game.homeScore);

          const isAway = game.awayTeam === selectedTeam?.abbrev;
          const isHome = game.homeTeam === selectedTeam?.abbrev;

          return (
            <div
              key={game.id}
              className={`game-card ${isSelected ? 'game-card--selected' : ''}`}
              onClick={() => onGameSelect(game)}
              style={{
                backgroundColor: isSelected ? `${teamColor}33` : undefined,
                borderColor: isSelected ? accentColor : undefined,
                boxShadow: isSelected ? `0 0 12px ${accentColor}4D` : undefined,
              }}
            >
              <div className="game-card-top">
                <span className="game-date">{game.date}</span>
                {selectedTeamWon && <span className="game-win-star">⭐</span>}
              </div>

              <div className="game-card-row">
                {game.awayLogo && (
                  <img
                    src={proxyUrl(game.awayLogo)}
                    alt={game.awayTeam}
                    className="game-team-logo"
                  />
                )}
                <span
                  className="game-team-name"
                  style={{
                    fontWeight: isAway ? 700 : 400,
                    color: isSelected && isAway ? accentColor : '#fff',
                  }}
                >
                  {game.awayTeam}
                </span>
                <span
                  className="game-team-score"
                  style={{
                    fontWeight: isAway ? 700 : 400,
                    color: isSelected && isAway ? accentColor : '#fff',
                  }}
                >
                  {game.awayScore}
                </span>
              </div>

              <div className="game-card-row">
                {game.homeLogo && (
                  <img
                    src={proxyUrl(game.homeLogo)}
                    alt={game.homeTeam}
                    className="game-team-logo"
                  />
                )}
                <span
                  className="game-team-name"
                  style={{
                    fontWeight: isHome ? 700 : 400,
                    color: isSelected && isHome ? accentColor : '#fff',
                  }}
                >
                  {game.homeTeam}
                </span>
                <span
                  className="game-team-score"
                  style={{
                    fontWeight: isHome ? 700 : 400,
                    color: isSelected && isHome ? accentColor : '#fff',
                  }}
                >
                  {game.homeScore}
                </span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
