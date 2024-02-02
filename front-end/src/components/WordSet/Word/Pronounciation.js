import React from 'react'

const Pronounciation = ({entry}) => {
  return (
    <>
          <h3>
            <b>{entry.entry}</b>
          </h3>

          {entry.pronunciations && entry.pronunciations.length > 0 && (
            <div className="pronunciation">
              {entry.pronunciations.map((pronunciation, pronunciationIndex) => (
                <div key={pronunciationIndex}>
                  {pronunciation.transcriptions &&
                    pronunciation.transcriptions.length > 0 && (
                      <div>
                        <p>{pronunciation.transcriptions[0].transcription}</p>
                        {pronunciation.audio && (
                          <audio controls>
                            <source
                              src={pronunciation.audio.url}
                              type="audio/mp3"
                            />
                          </audio>
                        )}
                      </div>
                    )}
                </div>
              ))}
            </div>
          )}
    </>
  )
}

export default Pronounciation